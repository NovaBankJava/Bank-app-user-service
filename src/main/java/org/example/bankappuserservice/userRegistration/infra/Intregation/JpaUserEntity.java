package org.example.bankappuserservice.adapter.outbound;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankappuserservice.userRegistration.domain.model.UserStatus;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class JpaUserEntity {

   @Id
   private String id;

   private String name;

   private String phone;

   @CPF
   @Column(unique = true)
   private String cpf;

   @Email
   @Column(unique = true)
   private String email;

   private String passwordHash;

   @Enumerated(EnumType.STRING)
   private UserStatus status;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
   private Instant createdAt;



}
