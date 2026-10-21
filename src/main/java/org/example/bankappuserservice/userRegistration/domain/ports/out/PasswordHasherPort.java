package org.example.bankappuserservice.userRegistration.domain.ports.out;

public interface PasswordHasherPort {

    String hash (String password);

}
