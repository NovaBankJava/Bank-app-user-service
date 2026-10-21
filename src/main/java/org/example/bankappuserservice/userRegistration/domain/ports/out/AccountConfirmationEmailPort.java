package org.example.bankappuserservice.userRegistration.domain.ports.out;

public interface AccountConfirmationEmailPort {

    void sendWelcomeEmail (String email, String name);
}
