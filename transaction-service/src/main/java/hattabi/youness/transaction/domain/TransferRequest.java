package hattabi.youness.transaction.domain;

import java.util.UUID;
import java.math.BigDecimal;

public record TransferRequest(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
}
