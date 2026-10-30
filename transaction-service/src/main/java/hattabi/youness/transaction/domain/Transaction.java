package hattabi.youness.transaction.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private String currency;
    private String merchantCategory;
    private String country;
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    private TransactionType type = TransactionType.DEBIT;

    @PrePersist
    void init() {
        if (id == null)
            id = UUID.randomUUID();
        createdAt = Instant.now();
    }
}
