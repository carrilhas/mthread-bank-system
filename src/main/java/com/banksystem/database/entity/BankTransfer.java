package com.banksystem.database.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BankTransfer {
    private String fromAccountId;
    private String toAccountId;
    private String type;
    private Long amount;
    private String correlationId;
}
