package com.banksystem.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Entity(name = "account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final String id;
    private final String accountId;
    private final String type;
    private float balance;
    @Version
    private Long version = 0L;

    public BankAccount() {
        this.id = UUID.randomUUID().toString();
        this.accountId = UUID.randomUUID().toString();
        this.type = null;
        this.balance = 0F;
    }
}
