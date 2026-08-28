package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.example.bankappuserservice.userRegistration.domain.ports.out.IdGeneratorPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorAdapter implements IdGeneratorPort {

    @Override
    public String generateId() {
       return UUID.randomUUID().toString();

    }
}
