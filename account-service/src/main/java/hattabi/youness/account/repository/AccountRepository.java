package hattabi.youness.account.repository;

import hattabi.youness.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}