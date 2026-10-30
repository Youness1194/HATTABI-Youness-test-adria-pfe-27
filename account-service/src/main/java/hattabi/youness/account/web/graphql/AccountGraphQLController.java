package hattabi.youness.account.web.graphql;

import hattabi.youness.account.service.*;
import hattabi.youness.account.domain.*;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AccountGraphQLController {
    private final AccountService service;

    @QueryMapping
    public List<Account> accounts() {
        return service.findAll();
    }

    @QueryMapping
    public Account account(@Argument UUID id) {
        return service.findById(id);
    }

    @MutationMapping
    public Account createAccount(@Argument String customerReference, @Argument String currency,
            @Argument double initialBalance) {
        return service.create(new CreateAccountRequest(customerReference, currency, initialBalance));
    }

    @MutationMapping
    public Account setAccountStatus(@Argument UUID id, @Argument AccountStatus status) {
        return service.updateStatus(id, status);
    }
}