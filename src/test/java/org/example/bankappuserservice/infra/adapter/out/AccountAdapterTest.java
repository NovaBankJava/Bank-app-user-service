package org.example.bankappuserservice.infra.adapter.out;

import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.account.domain.model.AccountType;
import org.example.bankappuserservice.infra.adapter.out.mapper.AccountOutMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AccountAdapter.class, AccountOutMapperImpl.class})
class AccountAdapterTest {

    @Autowired
    private AccountAdapter adapter;

    private final String userId = UUID.randomUUID().toString();

    @Test
    void savesAndFindsAccountByUserId() {
        adapter.save(newAccount());

        assertThat(adapter.findByUserId(userId))
                .hasSize(1)
                .first()
                .satisfies(found -> {
                    assertThat(found.getBank()).isEqualTo("NovaBank");
                    assertThat(found.getUserId()).isEqualTo(userId);
                    assertThat(found.getCreatedAt()).isNotNull();
                });
    }

    @Test
    void deletesAccountById() {
        Account account = newAccount();
        adapter.save(account);

        adapter.deleteById(account.getId());

        assertThat(adapter.findByUserId(userId)).isEmpty();
    }

    private Account newAccount() {
        return Account.create(userId, "NovaBank", "0001", "123456", AccountType.CHECKING);
    }
}