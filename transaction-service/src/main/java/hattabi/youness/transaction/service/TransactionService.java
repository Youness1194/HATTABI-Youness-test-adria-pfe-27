package hattabi.youness.transaction.service;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

import feign.FeignException;
import hattabi.youness.transaction.client.AccountClient;
import hattabi.youness.transaction.domain.AccountDto;
import hattabi.youness.transaction.domain.BalanceUpdate;
import hattabi.youness.transaction.domain.BankProperties;
import hattabi.youness.transaction.domain.BusinessException;
import hattabi.youness.transaction.domain.CreateTransactionRequest;
import hattabi.youness.transaction.domain.Transaction;
import hattabi.youness.transaction.domain.TransactionType;
import hattabi.youness.transaction.domain.TransferRequest;
import hattabi.youness.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repo;
    private final AccountClient accounts;
    private final BankProperties props;

    public List<Transaction> findAll() {
        return repo.findAll();
    }

    public List<Transaction> findByAccount(UUID accountId) {
        return repo.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    @Transactional
    public Transaction create(CreateTransactionRequest r) {
        checkAmount(r.amount());
        AccountDto acc = loadAccount(r.accountId());
        checkUsable(acc, r.currency());
        if (acc.balance() < r.amount().doubleValue())
            throw new BusinessException("Insufficient balance");

        Transaction t = new Transaction();
        t.setAccountId(r.accountId());
        t.setAmount(r.amount());
        t.setCurrency(r.currency());
        t.setMerchantCategory(r.merchantCategory());
        t.setCountry(r.country());
        t = repo.save(t);
        accounts.updateBalance(r.accountId(), new BalanceUpdate(-r.amount().doubleValue()));
        return t;
    }

    @Transactional
    public List<Transaction> transfer(TransferRequest r) {
        if (r.fromAccountId().equals(r.toAccountId()))
            throw new BusinessException("Same account");
        checkAmount(r.amount());
        AccountDto from = loadAccount(r.fromAccountId());
        AccountDto to = loadAccount(r.toAccountId());
        checkUsable(from, from.currency());
        checkUsable(to, from.currency());
        if (from.balance() < r.amount().doubleValue())
            throw new BusinessException("Insufficient balance");

        Transaction debit = save(r.fromAccountId(), r.amount(), from.currency(), TransactionType.DEBIT);
        Transaction credit = save(r.toAccountId(), r.amount(), from.currency(), TransactionType.CREDIT);

        double amt = r.amount().doubleValue();
        accounts.updateBalance(r.fromAccountId(), new BalanceUpdate(-amt));
        try {
            accounts.updateBalance(r.toAccountId(), new BalanceUpdate(amt));
        } catch (RuntimeException e) {
            accounts.updateBalance(r.fromAccountId(), new BalanceUpdate(amt));
            throw e;
        }
        return List.of(debit, credit);
    }

    private Transaction save(UUID acc, BigDecimal amount, String cur, TransactionType type) {
        Transaction t = new Transaction();
        t.setAccountId(acc);
        t.setAmount(amount);
        t.setCurrency(cur);
        t.setMerchantCategory("TRANSFER");
        t.setType(type);
        return repo.save(t);
    }

    private AccountDto loadAccount(UUID id) {
        try {
            return accounts.getAccount(id);
        } catch (FeignException.NotFound e) {
            throw new BusinessException("Account not found: " + id);
        }
    }

    private void checkAmount(BigDecimal a) {
        if (a == null || a.signum() <= 0)
            throw new BusinessException("Amount must be strictly positive");
        if (a.compareTo(props.maxAmount()) > 0)
            throw new BusinessException("Amount exceeds max " + props.maxAmount());
    }

    private void checkUsable(AccountDto acc, String currency) {
        if (!"ACTIVE".equals(acc.status()))
            throw new BusinessException("Account is not active");
        if (!acc.currency().equals(currency))
            throw new BusinessException("Currency mismatch");
    }
}
