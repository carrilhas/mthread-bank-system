package com.banksystem;

import com.banksystem.database.dto.BankAccountDto;
import com.banksystem.database.dto.BankTransferDto;
import com.banksystem.database.entity.BankAccount;
import com.banksystem.database.entity.BankTransfer;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BankTransferDto transfer(float amount, String fromAccountId, String toAccountId) {
        BankAccount fromBankAccount = accountRepository.findByAccountId(fromAccountId);
        BankAccount toBankAccount = accountRepository.findByAccountId(toAccountId);

        if (fromBankAccount != null && toBankAccount != null) {
            BankAccountDto fromAccountDto = convertBankAccountToDto(fromBankAccount);
            BankAccountDto toAccountDto = convertBankAccountToDto(toBankAccount);

            float withdrawn = fromAccountDto.withdraw(amount);

            if(withdrawn != fromAccountDto.getBalance()){
                logger.info("Withdrew {} from account {}", amount, fromAccountId);
                logger.info("Deposited {} into account {}", amount, toAccountId);

                fromBankAccount.setBalance(fromAccountDto.getBalance() - amount);
                toBankAccount.setBalance(toAccountDto.getBalance() + amount);
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

        return null;
    }

    public BankTransferDto getTransferById(Long transferId) {
        BankTransfer bankTransfer = transactionRepository.getReferenceById(transferId);

        if(bankTransfer == null){
            return null;
        }

        return convertBankTransferToDto(bankTransfer);
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
}
