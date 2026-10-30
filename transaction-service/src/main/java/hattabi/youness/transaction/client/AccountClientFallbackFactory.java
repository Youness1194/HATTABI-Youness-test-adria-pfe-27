package hattabi.youness.transaction.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import feign.FeignException;
import hattabi.youness.transaction.domain.AccountDto;
import hattabi.youness.transaction.domain.AccountServiceUnavailableException;
import hattabi.youness.transaction.domain.BalanceUpdate;

@Component
public class AccountClientFallbackFactory implements FallbackFactory<AccountClient> {
    public AccountClient create(Throwable cause) {
        if (cause instanceof FeignException fe && fe.status() >= 400 && fe.status() < 500)
            throw fe;
        return new AccountClient() {
            public AccountDto getAccount(UUID id) {
                throw new AccountServiceUnavailableException(cause);
            }

            public AccountDto updateBalance(UUID id, BalanceUpdate u) {
                throw new AccountServiceUnavailableException(cause);
            }
        };
    }
}
