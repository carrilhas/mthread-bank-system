package com.banksystem.database.dto;

import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BankAccountDto {

    private static final Logger logger = LogManager.getLogger(BankAccountDto.class);
    private String accountId;
    private String type;
    private float balance;
    private String correlationId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
