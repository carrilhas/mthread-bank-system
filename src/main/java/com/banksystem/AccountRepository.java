package com.banksystem;

import com.banksystem.database.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    @Query(value = "SELECT * FROM account WHERE account_id = :accountId", nativeQuery = true)
    BankAccount findByAccountId(@Param("accountId") String accountId);
}
