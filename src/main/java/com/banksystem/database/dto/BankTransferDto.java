package com.banksystem.database.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankTransferDto {
    private String fromAccountId;
    private String toAccountId;
    private String type;
    private Long amount;
    private String correlationId;
}
