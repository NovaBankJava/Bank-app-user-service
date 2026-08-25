package org.example.bankappuserservice.infra.adapter.out;

import org.example.bankappuserservice.domain.model.User;
import org.example.bankappuserservice.domain.exception.UserAlreadyExistsException;
import org.example.bankappuserservice.domain.ports.out.UserOutputPort;
import org.example.bankappuserservice.infra.adapter.out.mapper.UserOutputMapper;
import org.example.bankappuserservice.infra.integration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAdapter implements UserOutputPort {

    private final UserRepository repository;
    private final UserOutputMapper mapper;

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        try {
            var save = repository.save(entity);
            return mapper.toDomain(save);
        } catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException("Username or email already exists", exception);
        }
    }
}
