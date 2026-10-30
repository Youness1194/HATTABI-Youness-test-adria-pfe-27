package hattabi.youness.transaction.web.graphql;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import hattabi.youness.transaction.domain.CreateTransactionRequest;
import hattabi.youness.transaction.domain.Transaction;
import hattabi.youness.transaction.domain.TransferRequest;
import hattabi.youness.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TransactionGraphQLController {
    private final TransactionService service;

    @QueryMapping
    public List<Transaction> transactions(@Argument UUID accountId) {
        return accountId == null ? service.findAll() : service.findByAccount(accountId);
    }

    @MutationMapping
    public Transaction createTransaction(@Argument UUID accountId, @Argument double amount, @Argument String currency,
            @Argument String merchantCategory, @Argument String country) {
        return service.create(new CreateTransactionRequest(accountId, BigDecimal.valueOf(amount), currency,
                merchantCategory, country));
    }

    @MutationMapping
    public List<Transaction> transfer(@Argument UUID fromAccountId, @Argument UUID toAccountId,
            @Argument double amount) {
        return service.transfer(new TransferRequest(fromAccountId, toAccountId, BigDecimal.valueOf(amount)));
    }
}