package com.banksystem.database.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@Builder
public class BankAccountDto {

    private static final Logger logger = LogManager.getLogger(BankAccountDto.class);
    private String accountId;
    private String type;
    private Long balance;
    private String correlationId;
    private Lock lock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean withdraw(Long amount) {
        try {
            if(lock.tryLock(10, TimeUnit.SECONDS)){
                this.balance -= amount;
                logger.info("Withdrew {} from account {}", amount, this.accountId);
            }
        } catch (Exception e) {
            logger.error("Error withdrawing funds: {}", e.getMessage());
        } finally {
            lock.unlock();
        }
        return false;
    }

    public void deposit(Long amount) {
        try {
            if(lock.tryLock(10, TimeUnit.SECONDS)){
                this.balance += amount;
                logger.info("Deposited {} into account {}", amount, this.accountId);
            }
        } catch (Exception e) {
            logger.error("Error depositing funds: {}", e.getMessage());
        }finally {
            lock.unlock();
        }
    }
}
