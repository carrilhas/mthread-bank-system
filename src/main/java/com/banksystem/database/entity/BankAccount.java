package com.banksystem.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
@AllArgsConstructor
@Builder
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


    public boolean withdraw(float amount) {
        if(balance - amount >= 0) {
            balance -= amount;
            return true;
        } else return false;
    }

    public void deposit(float amount) {
        this.balance += amount;
    }
}
