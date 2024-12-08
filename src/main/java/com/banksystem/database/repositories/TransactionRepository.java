package com.banksystem.database.repositories;

import com.banksystem.database.entity.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("transactions")
public interface TransactionRepository extends JpaRepository<BankTransfer, String> {

    @Query("SELECT * FROM transactions WHERE t.correlationId = ?1")
    Optional<BankTransfer> getByCorrelationId(String correlationId);

    @Query("SELECT * FROM transactions WHERE (t.fromAccountId = ?1 OR t.toAccountId = ?1) AND t.date BETWEEN ?2 AND ?3")
    List<BankTransfer> findByDateRange(String accountId, LocalDate startDate, LocalDate endDate);

    BankTransfer deleteByCorrelationId(String correlationId);
}
