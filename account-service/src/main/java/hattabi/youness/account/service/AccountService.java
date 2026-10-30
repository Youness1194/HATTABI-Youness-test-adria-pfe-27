package hattabi.youness.account.service;

import hattabi.youness.account.repository.AccountRepository;
import hattabi.youness.account.domain.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repo;

    public List<Account> findAll() {
        return repo.findAll();
    }

    public Account findById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account create(CreateAccountRequest r) {
        Account a = new Account();
        a.setCustomerReference(r.customerReference());
        a.setCurrency(r.currency());
        a.setBalance(r.initialBalance());
        return repo.save(a);
    }

    public Account updateStatus(UUID id, AccountStatus status) {
        Account a = findById(id);
        a.setStatus(status);
        return repo.save(a);
    }

    @Transactional
    public Account adjustBalance(UUID id, double delta) {
        Account a = findById(id);
        if (a.getBalance() + delta < 0)
            throw new IllegalArgumentException("Insufficient balance");
        a.setBalance(a.getBalance() + delta);
        return repo.save(a);
    }
}
