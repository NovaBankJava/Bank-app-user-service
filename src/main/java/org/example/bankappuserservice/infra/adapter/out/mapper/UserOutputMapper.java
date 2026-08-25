package org.example.bankappuserservice.infra.adapter.out.mapper;

import org.example.bankappuserservice.domain.model.User;
import org.example.bankappuserservice.infra.integration.repository.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserOutputMapper {
    @Mapping(target = "newRecord", ignore = true)
    UserEntity toEntity(User user);
    User toDomain(UserEntity entity);
}
