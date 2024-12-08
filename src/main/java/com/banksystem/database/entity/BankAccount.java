package com.banksystem.database.entity;

import com.banksystem.database.dto.BankAccountDto;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Builder
@Entity
public class BankAccount {
    private final String accountId;
    private final String type;
    private final Long balance;
    private final String correlationId;
}
