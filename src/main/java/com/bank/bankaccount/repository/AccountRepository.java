package com.bank.bankaccount.repository;

import com.bank.bankaccount.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
   Account getById(Long id);
}
