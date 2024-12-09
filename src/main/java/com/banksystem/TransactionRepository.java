package com.banksystem;

import com.banksystem.database.entity.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("transfer")
public interface TransactionRepository extends JpaRepository<BankTransfer, Long> {

    BankTransfer deleteByCorrelationId(String correlationId);
}
