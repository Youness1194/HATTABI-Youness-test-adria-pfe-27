package hattabi.youness.transaction.domain;

public class AccountServiceUnavailableException extends RuntimeException {
    public AccountServiceUnavailableException(Throwable c) {
        super("account-service is unavailable", c);
    }
}
