package org.example.bankappuserservice.infra.integration.repository;

import org.example.bankappuserservice.infra.integration.repository.entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<UserEntity, String> {
}
