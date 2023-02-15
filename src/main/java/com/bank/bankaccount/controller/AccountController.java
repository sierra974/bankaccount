package com.bank.bankaccount.controller;

import com.bank.bankaccount.entity.Account;
import com.bank.bankaccount.entity.Operation;
import com.bank.bankaccount.entity.TypeOperation;
import com.bank.bankaccount.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/list")
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @GetMapping("/{pId}")
    public Account get(@PathVariable Long pId) {

        Optional<Account> account = accountRepository.findById(pId);

        if (account.isPresent()) {
            return account.get();
        }
        return null;
    }

    @PostMapping("/add")
    public Account add(@RequestBody String name) {

        Account account = new Account();
        account.setName(name);

        return accountRepository.save(account);
    }

    /*
     US 1:
        In order to save money
        As a bank client
        I want to make a deposit in my account
     */
    @PostMapping("/{id}/deposit/{val}")
    public ResponseEntity<?> deposit(@PathVariable Long id, @PathVariable Double val) {

        Account account = accountRepository.getById(id);
        Double newBalance = account.getBalance() + val;

        Operation op = new Operation();
        op.setTypeOperation(TypeOperation.DEPOSIT);
        op.setPreviousBalance(account.getBalance());
        op.setNewBalance(newBalance);
        op.setAmount(val);
        op.setDate(new Date());

        account.setBalance(newBalance);
        account.getOperations().add(op);

        accountRepository.save(account);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /*
     US 2:
        In order to retrieve some or all of my savings
        As a bank client
        I want to make a withdrawal from my account
    */
    @PostMapping("/{id}/withdrawal/{val}")
    public ResponseEntity<?> withdrawal(@PathVariable Long id, @PathVariable Double val) {

        Account account = accountRepository.getById(id);
        Double newBalance = account.getBalance() - val;

        Operation op = new Operation();
        op.setTypeOperation(TypeOperation.WITHDRAWAL);
        op.setPreviousBalance(account.getBalance());
        op.setNewBalance(newBalance);
        op.setAmount(val);
        op.setDate(new Date());

        account.setBalance(newBalance);
        account.getOperations().add(op);

        accountRepository.save(account);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /*
     US 3:
        In order to check my operations
        As a bank client
        I want to see the history (operation, date, amount, balance) of my operations
     */
    @GetMapping("/{id}/operations")
    public List<Operation> listOperations(@PathVariable Long id) {

        Account account = accountRepository.getById(id);
        return account.getOperations();
    }

    /* ******************************************************** */
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/testAddClient")
    public Account testAdd() {

        Account account = new Account();
        account.setBalance(500.0);
        account.setName("Client 1");

        return accountRepository.save(account);
    }

}
