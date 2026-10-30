package hattabi.youness.account.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    private UUID id;
    private String customerReference;
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;
    private String currency;
    private double balance;

    @PrePersist
    void init() {
        if (id == null)
            id = UUID.randomUUID();
    }
}
