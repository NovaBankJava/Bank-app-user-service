package org.example.bankappuserservice.account.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountTest {

    @Test
    void newAccountStartsAsNonPrimary() {
        Account account = Account.create(
                UUID.randomUUID().toString(), "NovaBank", "0001", "123456", AccountType.CHECKING);

        assertThat(account.isPrimary()).isFalse();
    }

    @Test
    void creatingAccountWithBlankBankFails() {
        assertThatThrownBy(() -> Account.create(
                UUID.randomUUID().toString(), "  ", "0001", "123456", AccountType.CHECKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void markAsPrimaryTurnsAccountIntoPrimary() {
        Account account = Account.create(
                UUID.randomUUID().toString(), "NovaBank", "0001", "123456", AccountType.CHECKING);

        account.markAsPrimary();

        assertThat(account.isPrimary()).isTrue();
    }

    @Test
    void unmarkAsPrimaryTurnsAccountIntoNonPrimary() {
        Account account = Account.create(
                UUID.randomUUID().toString(), "NovaBank", "0001", "123456", AccountType.CHECKING);
        account.markAsPrimary();

        account.unmarkAsPrimary();

        assertThat(account.isPrimary()).isFalse();
    }

    @Test
    void belongsToReturnsTrueForOwnerAndFalseForOthers() {
        String ownerId = UUID.randomUUID().toString();
        Account account = Account.create(
                ownerId, "NovaBank", "0001", "123456", AccountType.CHECKING);

        assertThat(account.belongsTo(ownerId)).isTrue();
        assertThat(account.belongsTo("someone-else")).isFalse();
    }
}
