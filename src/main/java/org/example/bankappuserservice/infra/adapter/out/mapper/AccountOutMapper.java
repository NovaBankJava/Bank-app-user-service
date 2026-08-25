package org.example.bankappuserservice.infra.adapter.out.mapper;

import org.example.bankappuserservice.account.domain.model.Account;
import org.example.bankappuserservice.infra.repository.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountOutMapper {

    AccountEntity toJpa(Account account);

    Account toDomain(AccountEntity entity);
}