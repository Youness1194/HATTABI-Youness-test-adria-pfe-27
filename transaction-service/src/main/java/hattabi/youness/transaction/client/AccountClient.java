package hattabi.youness.transaction.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import hattabi.youness.transaction.domain.AccountDto;
import hattabi.youness.transaction.domain.BalanceUpdate;

@FeignClient(name = "account-service", fallbackFactory = AccountClientFallbackFactory.class)
public interface AccountClient {
    @GetMapping("/api/accounts/{id}")
    AccountDto getAccount(@PathVariable("id") UUID id);

    @PutMapping("/api/accounts/{id}/balance")
    AccountDto updateBalance(@PathVariable("id") UUID id, @RequestBody BalanceUpdate u);
}