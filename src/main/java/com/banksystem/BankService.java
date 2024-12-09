package com.banksystem;

import com.banksystem.database.dto.BankAccountDto;
import com.banksystem.database.dto.BankTransferDto;
import com.banksystem.database.entity.BankAccount;
import com.banksystem.database.entity.BankTransfer;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@AllArgsConstructor
@Service
public class BankService {

    @Autowired(required = false)
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankServiceMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(BankService.class);
    private final ConcurrentHashMap<String, ReentrantLock> accountLocks = new ConcurrentHashMap<>();


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BankAccountDto createAccount(String type, float balance) {
        BankAccount bankAccount = BankAccount.builder()
                .accountId(UUID.randomUUID().toString())
                .type(type)
                .balance(balance)
                .build();
        return convertBankAccountToDto(accountRepository.save(bankAccount));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BankTransferDto transfer(float amount, String fromAccountId, String toAccountId) {
        ReentrantLock fromLock = getLock(fromAccountId);
        ReentrantLock toLock = getLock(toAccountId);

        fromLock.lock();
        try {
            toLock.lock();
            try {
                BankAccount fromBankAccount = accountRepository.findByAccountId(fromAccountId);
                BankAccount toBankAccount = accountRepository.findByAccountId(toAccountId);

                if (fromBankAccount != null && toBankAccount != null) {
                    if (fromBankAccount.withdraw(amount)) {
                        toBankAccount.deposit(amount);

                        logger.info("Withdrew {} from account {}", amount, fromAccountId);
                        logger.info("Deposited {} into account {}", amount, toAccountId);
                    }

                    BankTransfer transfer = BankTransfer.builder()
                            .fromAccountId(fromAccountId)
                            .toAccountId(toAccountId)
                            .amount(amount)
                            .correlationId(UUID.randomUUID().toString())
                            .build();

                    accountRepository.save(fromBankAccount);
                    accountRepository.save(toBankAccount);
                    return convertBankTransferToDto(transactionRepository.save(transfer));
                }
            } finally {
                toLock.unlock();
            }
        } finally {
            fromLock.unlock();
        }
        return null;
    }

    public BankTransferDto cancelTransfer(String transferId) {
        return convertBankTransferToDto(transactionRepository.deleteByCorrelationId(transferId));
    }

    private BankTransferDto convertBankTransferToDto(BankTransfer bankTransfer) {
        return modelMapper.map(bankTransfer, BankTransferDto.class);
    }

    private BankAccountDto convertBankAccountToDto(BankAccount bankAccount) {
        return modelMapper.map(bankAccount, BankAccountDto.class);
    }

    private ReentrantLock getLock(String accountId) {
        return accountLocks.computeIfAbsent(accountId, id -> new ReentrantLock());
    }
}
