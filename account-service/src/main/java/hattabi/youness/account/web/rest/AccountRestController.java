package hattabi.youness.account.web.rest;

import hattabi.youness.account.domain.*;
import hattabi.youness.account.service.*;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {
    private final AccountService service;

    @GetMapping
    public List<Account> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Account one(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody CreateAccountRequest r) {
        return service.create(r);
    }

    @PatchMapping("/{id}/status")
    public Account status(@PathVariable UUID id, @RequestParam AccountStatus value) {
        return service.updateStatus(id, value);
    }

    @PutMapping("/{id}/balance")
    public Account balance(@PathVariable UUID id, @RequestBody BalanceUpdate u) {
        return service.adjustBalance(id, u.delta());
    }
}
