package org.example.bankappuserservice.infra.adapter.out.crypto;

import org.example.bankappuserservice.domain.ports.out.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String value) {
        return passwordEncoder.encode(value);
    }
}
