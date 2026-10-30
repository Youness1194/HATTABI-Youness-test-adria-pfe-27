package hattabi.youness.transaction.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.math.BigDecimal;

@ConfigurationProperties(prefix = "bank.transaction")
public record BankProperties(BigDecimal maxAmount) {
}
