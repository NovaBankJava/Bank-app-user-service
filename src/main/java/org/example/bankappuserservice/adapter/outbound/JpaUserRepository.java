package org.example.bankappuserservice.adapter.outbound;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository <JpaUserEntity, String>{

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);


}
