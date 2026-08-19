package org.example.bankappuserservice.account.adapter.out.user;

import lombok.extern.slf4j.Slf4j;
import org.example.bankappuserservice.account.application.port.out.UserLookupPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Slf4j
public class StubUserLookupAdapter implements UserLookupPort {

    public static final String STUB_CPF = "00000000000";

    @Override
    public Optional<UserData> findByUserId(String userId) {
        log.warn("Using STUB user lookup for user: {} — replace when user feature is ready", userId);
        return Optional.of(new UserData(userId, STUB_CPF, LocalDate.of(1990, 1, 1)));
    }
}