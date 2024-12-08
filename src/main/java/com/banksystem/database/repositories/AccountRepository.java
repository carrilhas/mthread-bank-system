package com.banksystem.database.repositories;

import com.banksystem.database.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("accounts")
public interface AccountRepository extends JpaRepository<BankAccount, String> {
}
