package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.example.bankappuserservice.userRegistration.infra.Integration.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository <JpaUserEntity, String>{

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);


}
