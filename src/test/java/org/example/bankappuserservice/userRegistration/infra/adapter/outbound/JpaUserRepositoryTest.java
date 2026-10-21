package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import jakarta.persistence.EntityManager;
import org.example.bankappuserservice.userRegistration.domain.model.UserStatus;
import org.example.bankappuserservice.userRegistration.infra.Integration.JpaUserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository repository;

    @Autowired
    private EntityManager entityManager;

    private JpaUserEntity userEntity;

    private Instant createdAt;

    @BeforeEach
    void setup() {

        createdAt = Instant.parse("2026-08-31T20:00:00Z");

        userEntity = new JpaUserEntity(
                "teste-id",
                "user teste",
                "75998887777",
                "233.543.771-27",
                "teste@gmail.com",
                "passwordHash",
                UserStatus.PENDING_VERIFICATION,
                createdAt);

    }

    @Test
    void shouldSaveUser() {

        JpaUserEntity savedEntity = repository.save(userEntity);

        assertNotNull(savedEntity);
        assertEquals("teste-id", savedEntity.getId());
        assertEquals("user teste", savedEntity.getName());
        assertEquals("75998887777", savedEntity.getPhone());
        assertEquals("233.543.771-27", savedEntity.getCpf());
        assertEquals("teste@gmail.com", savedEntity.getEmail());
        assertEquals("passwordHash", savedEntity.getPasswordHash());
        assertEquals(UserStatus.PENDING_VERIFICATION, savedEntity.getStatus());
        assertEquals(createdAt,savedEntity.getCreatedAt());


        entityManager.flush();
        entityManager.clear();

        Optional<JpaUserEntity> rescuedEntity = repository.findById("teste-id");

        assertTrue(rescuedEntity.isPresent());

        JpaUserEntity foundEntity = rescuedEntity.get();

        assertEquals("teste-id", foundEntity.getId());
        assertEquals("user teste", foundEntity.getName());
        assertEquals("75998887777", foundEntity.getPhone());
        assertEquals("233.543.771-27", foundEntity.getCpf());
        assertEquals("teste@gmail.com", foundEntity.getEmail());
        assertEquals("passwordHash", foundEntity.getPasswordHash());
        assertEquals(UserStatus.PENDING_VERIFICATION, foundEntity.getStatus());
        assertEquals(createdAt, foundEntity.getCreatedAt());

    }

    @Test
    void shouldReturnTrueWhenCpfExists() {

        repository.save(userEntity);

        entityManager.flush();
        entityManager.clear();

        boolean result = repository.existsByCpf(userEntity.getCpf());

        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenCpfDoesNotExist() {

        String nonExistingCpf = "869.355.390-99";

        boolean result = repository.existsByCpf(nonExistingCpf);

        assertFalse(result);

    }

    @Test
    void shouldThrowExceptionWhenCpfIsDuplicated() {

        repository.save(userEntity);

        JpaUserEntity userWithDuplicatedCpf = new JpaUserEntity(
                "duplicate-id",
                "user duplicate",
                "75998887776",
                "233.543.771-27",
                "duplicated@gmail.com",
                "passwordHash",
                UserStatus.PENDING_VERIFICATION,
                createdAt
        );

        repository.save(userWithDuplicatedCpf);

        assertThrows(
                DataIntegrityViolationException.class,
                ()->repository.flush()

        );

    }

    @Test
    void shouldReturnTrueWhenEmailExists() {

        repository.save(userEntity);

        entityManager.flush();
        entityManager.clear();

        boolean result = repository.existsByEmail(userEntity.getEmail());

        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist () {

        String nonExistingEmail = "noExisting@gmail.com";

        boolean result = repository.existsByEmail(nonExistingEmail);

        assertFalse(result);

    }

    @Test
    void shouldThrowExceptionWhenEmailIsDuplicated() {

        repository.save(userEntity);

        JpaUserEntity userWithDuplicatedEmail = new JpaUserEntity(
                "duplicate-id",
                "user duplicate",
                "75998887776",
                "869.355.390-99",
                "teste@gmail.com",
                "passwordHash",
                UserStatus.PENDING_VERIFICATION,
                createdAt
        );

        repository.save(userWithDuplicatedEmail);

        assertThrows(
                DataIntegrityViolationException.class,
                ()-> repository.flush());

    }

}
