package com.omoke.store.mappers;

import com.omoke.store.dtos.RegisterUserRequest;
import com.omoke.store.dtos.UpdateUserRequest;
import com.omoke.store.dtos.UserDto;
import com.omoke.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUserEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
