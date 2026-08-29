package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.example.bankappuserservice.userRegistration.domain.ports.out.UserRepositoryPort;
import org.example.bankappuserservice.userRegistration.domain.model.User;
import org.example.bankappuserservice.userRegistration.infra.Intregation.JpaUserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserPersistenceAdapter(JpaUserRepository jpaUserRepository, UserPersistenceMapper userPersistenceMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }


    @Override
    public boolean existsByCpf(String cpf) {
        return jpaUserRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        JpaUserEntity entity = userPersistenceMapper.toEntity(user);
        JpaUserEntity savedEntity = jpaUserRepository.save(entity);
        return userPersistenceMapper.toDomain(savedEntity);
    }
}
