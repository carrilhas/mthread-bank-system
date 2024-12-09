package com.banksystem.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Entity
public class BankTransfer {
    @Id
    private String id;
    private String fromAccountId;
    private String toAccountId;
    private Long amount;
    private String correlationId;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
