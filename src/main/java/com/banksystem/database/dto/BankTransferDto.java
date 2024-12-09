package com.banksystem.database.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankTransferDto {
    private Long id;
    private String fromAccountId;
    private String toAccountId;
    private float amount;
    private String correlationId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
