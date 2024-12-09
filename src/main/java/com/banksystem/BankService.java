package com.banksystem;

import com.banksystem.database.dto.BankAccountDto;
import com.banksystem.database.dto.BankTransferDto;
import com.banksystem.database.entity.BankAccount;
import com.banksystem.database.entity.BankTransfer;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BankAccountDto createAccount(String type, Long balance) {
        BankAccountDto bankAccountDto = BankAccountDto.builder()
                .type(type)
                .balance(balance)
                .accountId(UUID.randomUUID().toString())
                .build();


        BankAccount account = accountRepository.save(convertBankAccountToEntity(bankAccountDto));
        return convertBankAccountToDto(account);
    }

    public BankTransferDto transfer(Long amount, String fromAccountId, String toAccountId) {
        BankAccount fromBankAccount = accountRepository.findById(fromAccountId).orElse(null);
        BankAccount toBankAccount = accountRepository.findById(toAccountId).orElse(null);

        if (fromBankAccount != null && toBankAccount != null) {
            BankAccountDto fromAccountDto = convertBankAccountToDto(fromBankAccount);
            BankAccountDto toAccountDto = convertBankAccountToDto(toBankAccount);

            if(fromAccountDto.withdraw(amount)){
                toAccountDto.deposit(amount);
            }

            accountRepository.save(convertBankAccountToEntity(fromAccountDto));
            accountRepository.save(convertBankAccountToEntity(toAccountDto));

            BankTransfer transfer = BankTransfer.builder()
                    .fromAccountId(fromAccountId)
                    .toAccountId(toAccountId)
                    .amount(amount)
                    .build();

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

    public List<BankTransferDto> getTransfersByDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByToAccountIdAndCreatedAtBetween(accountId, startDate, endDate)
                .stream()
                .map(this::convertBankTransferToDto)
                .toList();
    }

    public BankTransferDto cancelTransfer(String transferId) {
        return convertBankTransferToDto(transactionRepository.deleteByCorrelationId(transferId));
    }

    private BankAccount convertBankAccountToEntity(BankAccountDto bankAccountDto) {
        return modelMapper.map(bankAccountDto, BankAccount.class);
    }

    private BankTransferDto convertBankTransferToDto(BankTransfer bankTransfer) {
        return modelMapper.map(bankTransfer, BankTransferDto.class);
    }

    private BankAccountDto convertBankAccountToDto(BankAccount bankAccount) {
        return modelMapper.map(bankAccount, BankAccountDto.class);
    }
}
