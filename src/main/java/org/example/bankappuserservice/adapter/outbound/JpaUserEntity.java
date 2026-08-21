package org.example.bankappuserservice.adapter.outbound;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankappuserservice.userRegistration.domain.model.AccountStatus;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class JpaUserEntity {

    @Id
   private String id;
   private String name;
   @CPF
   private String cpf;
   @Email
   private String email;
   private String password;
   @Enumerated(EnumType.STRING)
   private AccountStatus status;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
   private LocalDateTime createAt;



}
