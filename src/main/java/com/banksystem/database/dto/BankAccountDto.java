package com.banksystem.database.dto;

import jakarta.persistence.LockModeType;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    @Builder.Default
    private Lock lock = new ReentrantLock();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public float withdraw(float amount) {
        try {
            if(lock.tryLock(10, TimeUnit.SECONDS)){
                return balance - amount >= 0 ? balance - amount : balance;
            }
        } catch (Exception e) {
            logger.error("Error withdrawing funds: {}", e.getMessage());
        } finally {
            lock.unlock();
        }
        return balance;
    }

    public void deposit(float amount) {
        try {
            if(lock.tryLock(10, TimeUnit.SECONDS)){
                this.balance += amount;
            }
        } catch (Exception e) {
            logger.error("Error depositing funds: {}", e.getMessage());
        }finally {
            lock.unlock();
        }
    }
}
