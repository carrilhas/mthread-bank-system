package com.banksystem.controller;

import com.banksystem.database.dto.BankTransferDto;
import com.banksystem.database.entity.BankTransfer;
import com.banksystem.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
public class BankSystemController {

    @Autowired
    private BankService bankService;

    @PostMapping("/transfers")
    public String transfer(Long amount, String fromAccountId, String toAccountId) {
        String transferId = bankService.transfer(amount, fromAccountId, toAccountId).getCorrelationId();

        return "Transfer successfully created with id: " + transferId;
    }

    @PostMapping("/transfers")
    public String getTransferById(String transferId) {
        BankTransferDto transfer = bankService.getTransferById(transferId);

        return transfer == null ? "Transfer not found" : "Transfer found: " + transfer;
    }

    @GetMapping("/transfers")
    public String getTransfers(String accountId, LocalDate startDate, LocalDate endDate) {
        List<BankTransferDto> transfers = bankService.getTransfersByDateRange(accountId, startDate, endDate);

        return transfers.isEmpty() ? "No transfers found" : "Transfers found: " + transfers;
    }

    @DeleteMapping("/transfers")
    public BankTransferDto cancelTransfer(String transferId) {
        return bankService.cancelTransfer(transferId);
    }
}
