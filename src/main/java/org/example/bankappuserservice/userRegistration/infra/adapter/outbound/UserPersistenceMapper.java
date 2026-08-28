package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.example.bankappuserservice.userRegistration.domain.model.User;
import org.example.bankappuserservice.userRegistration.infra.Intregation.JpaUserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public JpaUserEntity toEntity (User user) {
        return new JpaUserEntity(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getCpf(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getStatus(),
                user.getCreatedAt());
    }

    public User toDomain (JpaUserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getPasswordHash(),
                entity.getStatus(),
                entity.getCreatedAt());
    }

}
