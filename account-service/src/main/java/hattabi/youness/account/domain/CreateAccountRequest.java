package hattabi.youness.account.domain;

public record CreateAccountRequest(String customerReference, String currency, double initialBalance) {
}