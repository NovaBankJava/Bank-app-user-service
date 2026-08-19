package org.example.bankappuserservice.account.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, String> {

    List<AccountJpaEntity> findByUserId(String userId);

    List<AccountJpaEntity> findByUserIdAndPrimaryTrue(String userId);

    boolean existsByUserId(String userId);

    boolean existsByUserIdAndBankAndBranchAndAccountNumber(
            String userId, String bank, String branch, String accountNumber);
}