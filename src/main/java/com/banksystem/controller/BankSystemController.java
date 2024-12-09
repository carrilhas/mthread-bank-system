package com.banksystem.controller;

import com.banksystem.database.dto.BankAccountDto;
import com.banksystem.database.dto.BankTransferDto;
import com.banksystem.BankService;
import com.banksystem.model.TransferRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
public class BankSystemController {

    @Autowired
    private BankService bankService;

    /**
     * @PostMapping(path = "/accounts")
     *     public BankAccountDto createAccount(String type, Long balance) {
     *         return bankService.createAccount(type, balance);
     *     }
     */


    @PostMapping(path = "/transfers")
    public String transfer(@RequestBody TransferRequest request) {
        BankTransferDto bankTransferDto = bankService.transfer(
                request.getAmount(),
                request.getFromAccountId(),
                request.getToAccountId());

        return "Transfer successfully created : " + bankTransferDto;
    }

    @GetMapping(path = "/transfers", params = {"transferId"})
    public String getTransferById(Long transferId) {
        BankTransferDto transfer = bankService.getTransferById(transferId);

        return transfer == null ? "Transfer not found" : "Transfer found: " + transfer;
    }

    @DeleteMapping(path = "/transfers", params = {"transferId"})
    public BankTransferDto cancelTransfer(String transferId) {
        return bankService.cancelTransfer(transferId);
    }
}
