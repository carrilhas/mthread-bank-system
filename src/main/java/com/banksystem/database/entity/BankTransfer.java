package com.banksystem.database.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Entity(name = "transfer")
@Getter
public class BankTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromAccountId;
    private String toAccountId;
    @Column(name = "transfer_amount")
    private float amount;
    private String correlationId;
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
