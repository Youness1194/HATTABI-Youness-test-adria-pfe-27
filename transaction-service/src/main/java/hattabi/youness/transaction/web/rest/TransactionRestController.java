package hattabi.youness.transaction.web.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hattabi.youness.transaction.domain.CreateTransactionRequest;
import hattabi.youness.transaction.domain.Transaction;
import hattabi.youness.transaction.domain.TransferRequest;
import hattabi.youness.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionRestController {
    private final TransactionService service;

    @GetMapping
    public List<Transaction> all(@RequestParam(required = false) UUID accountId) {
        return accountId == null ? service.findAll() : service.findByAccount(accountId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@RequestBody CreateTransactionRequest r) {
        return service.create(r);
    }

    @PostMapping("/transfer")
    public List<Transaction> transfer(@RequestBody TransferRequest r) {
        return service.transfer(r);
    }
}
