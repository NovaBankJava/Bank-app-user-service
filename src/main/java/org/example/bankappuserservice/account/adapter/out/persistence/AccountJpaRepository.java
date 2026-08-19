package org.example.bankappuserservice.account.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, String> {

    List<AccountJpaEntity> findByUserId(String userId);

    List<AccountJpaEntity> findByUserIdAndPrimaryTrue(String userId);

    boolean existsByUserId(String userId);
}