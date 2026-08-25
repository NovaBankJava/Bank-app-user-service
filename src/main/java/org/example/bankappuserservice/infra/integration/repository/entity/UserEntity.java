package org.example.bankappuserservice.infra.integration.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.domain.Persistable;
import org.springframework.data.annotation.Transient;

import java.time.Instant;

@Data
@Table(name = "tb_user", schema = "user")
public class UserEntity implements Persistable<String> {
    @Id
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private Instant createdAt;

    @Transient
    private boolean newRecord = true;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return newRecord;
    }

    public void markNotNew() {
        this.newRecord = false;
    }
}
