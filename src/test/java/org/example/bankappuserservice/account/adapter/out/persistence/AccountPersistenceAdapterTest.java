package org.example.bankappuserservice.account.adapter.out.persistence;

import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountPersistenceMapper.class})
class AccountPersistenceAdapterTest {

    @Autowired
    private AccountPersistenceAdapter adapter;

    private final String userId = UUID.randomUUID().toString();

    @Test
    void savesAndFindsAccountByUserId() {
        Account account = Account.create(
                userId, "NovaBank", "0001", "123456", AccountType.CHECKING);

        adapter.save(account);

        assertThat(adapter.findByUserId(userId))
                .hasSize(1)
                .first()
                .satisfies(found -> {
                    assertThat(found.getBank()).isEqualTo("NovaBank");
                    assertThat(found.getUserId()).isEqualTo(userId);
                });
    }

    @Test
    void deletesAccountById() {
        Account account = Account.create(
                userId, "NovaBank", "0001", "123456", AccountType.CHECKING);
        adapter.save(account);

        adapter.deleteById(account.getId());

        assertThat(adapter.findByUserId(userId)).isEmpty();
    }
}