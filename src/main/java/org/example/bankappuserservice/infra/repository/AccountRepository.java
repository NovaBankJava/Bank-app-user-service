package org.example.bankappuserservice.infra.repository;

import org.example.bankappuserservice.infra.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    List<AccountEntity> findByUserId(String userId);

    List<AccountEntity> findByUserIdAndPrimaryTrue(String userId);

    boolean existsByUserId(String userId);

    boolean existsByUserIdAndBankAndBranchAndAccountNumber(
            String userId, String bank, String branch, String accountNumber);
}