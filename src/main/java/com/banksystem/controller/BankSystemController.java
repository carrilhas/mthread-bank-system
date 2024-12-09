package com.banksystem.controller;

import com.banksystem.database.dto.BankAccountDto;
import com.banksystem.database.dto.BankTransferDto;
import com.banksystem.BankService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
public class BankSystemController {

    @Autowired
    private BankService bankService;

    @PostMapping(path = "/accounts", params = {"type", "balance"})
    public BankAccountDto createAccount(String type, Long balance) {
        return bankService.createAccount(type, balance);
    }

    @PostMapping(path = "/transfers", params = {"amount", "fromAccountId", "toAccountId"})
    public String transfer(Long amount, String fromAccountId, String toAccountId) {
        String transferId = bankService.transfer(amount, fromAccountId, toAccountId).getCorrelationId();

        return "Transfer successfully created with id: " + transferId;
    }

    @GetMapping(path = "/transfers", params = {"transferId"})
    public String getTransferById(Long transferId) {
        BankTransferDto transfer = bankService.getTransferById(transferId);

        return transfer == null ? "Transfer not found" : "Transfer found: " + transfer;
    }

    @GetMapping(path = "/transfers", params = {"accountId", "startDate", "endDate"})
    public String getTransfersByDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<BankTransferDto> transfers = bankService.getTransfersByDateRange(accountId, startDate, endDate);

        return transfers.isEmpty() ? "No transfers found" : "Transfers found: " + transfers;
    }

    @DeleteMapping(path = "/transfers", params = {"transferId"})
    public BankTransferDto cancelTransfer(String transferId) {
        return bankService.cancelTransfer(transferId);
    }
}
