package com.banksystem.BankSystem;

import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Data
public class BankAccount {
    private static final Logger logger = LogManager.getLogger(BankAccount.class);
    private String accountId;
    private String type;
    private Long balance;
    private String correlationId;
    private Lock lock;

    public BankAccount(String accountId, String type, Long balance, String correlationId) {
        this.accountId = accountId;
        this.type = type;
        this.balance = balance;
        this.correlationId = correlationId;
        this.lock = new ReentrantLock();
    }

    public void withdraw(Long amount) {
        try {
            if(lock.tryLock(10, TimeUnit.SECONDS)){
                this.balance -= amount;
                logger.info("Withdrew " + amount + " from account " + this.accountId);
            }
        } catch (Exception e) {
            logger.error("Error withdrawing funds: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void deposit(Long amount) {
        try {
            if(lock.tryLock(10, TimeUnit.SECONDS)){
                this.balance += amount;
                logger.info("Deposited " + amount + " into account " + this.accountId);
            }
        } catch (Exception e) {
            logger.error("Error depositing funds: " + e.getMessage());
        }finally {
            lock.unlock();
        }
    }
}
