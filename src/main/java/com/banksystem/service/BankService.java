package com.banksystem.service;

import com.banksystem.database.dto.BankAccountDto;
import com.banksystem.database.dto.BankTransferDto;
import com.banksystem.database.repositories.AccountRepository;
import com.banksystem.database.repositories.TransactionRepository;
import com.banksystem.database.entity.BankAccount;
import com.banksystem.database.entity.BankTransfer;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class BankService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ModelMapper modelMapper;

    public BankAccountDto createAccount(String type, Long balance, String correlationId) {
        BankAccount bankAccount = BankAccount.builder()
                .type(type)
                .balance(balance)
                .correlationId(correlationId)
                .build();

        return convertBankAccountToDto(accountRepository.save(bankAccount));
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

    public BankTransferDto getTransferById(String transferId) {
        BankTransfer bankTransfer = transactionRepository.getByCorrelationId(transferId).orElse(null);

        if(bankTransfer == null){
            return null;
        }

        return convertBankTransferToDto(bankTransfer);
    }

    public List<BankTransferDto> getTransfersByDateRange(String accountId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateRange(accountId, startDate, endDate)
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
