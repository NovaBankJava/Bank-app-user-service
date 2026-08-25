package org.example.bankappuserservice.infra.adapter.in.mapper;

import org.example.bankappuserservice.domain.model.CreateUserCommand;
import org.example.bankappuserservice.infra.adapter.in.dto.UserPostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserInboundMapper {
    CreateUserCommand toCommand(UserPostRequest request);
}
