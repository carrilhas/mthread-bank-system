package com.banksystem.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Entity(name = "account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final String id;
    private final String accountId;
    private final String type;
    private final Long balance;

    public BankAccount() {
        this.id = UUID.randomUUID().toString();
        this.accountId = UUID.randomUUID().toString();
        this.type = null;
        this.balance = null;
    }
}
