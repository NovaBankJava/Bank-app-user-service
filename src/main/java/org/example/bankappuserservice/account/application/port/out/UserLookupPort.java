package org.example.bankappuserservice.account.application.port.out;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public interface UserLookupPort {

    Optional<UserData> findByUserId(String userId);

    record UserData(String userId, String cpf, LocalDate birthDate) {

        public int ageInYears() {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }

        public boolean isMinor() {
            return ageInYears() < 18;
        }
    }
}