package com.banksystem.model;

import lombok.Data;

@Data
public class TransferRequest {
    private float amount;
    private String fromAccountId;
    private String toAccountId;
}
