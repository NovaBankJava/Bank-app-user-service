package org.example.bankappuserservice.userRegistration.application.port.out;

public interface PasswordHasherPort {

    String hash (String password);

}
