package com.omoke.store.mappers;

import com.omoke.store.dtos.UserDto;
import com.omoke.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

}
