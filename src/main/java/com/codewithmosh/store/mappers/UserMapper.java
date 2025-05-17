package com.codewithmosh.store.mappers;

import com.codewithmosh.store.Dtos.UserDto;
import com.codewithmosh.store.Dtos.UserRegisterRequest;
import com.codewithmosh.store.Dtos.UserUpdateRequest;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel ="spring" )
public interface UserMapper {
    UserDto toUserDto(User user);
    User toEntity(UserRegisterRequest entity);
    void update(UserUpdateRequest request , @MappingTarget User user  );
}
