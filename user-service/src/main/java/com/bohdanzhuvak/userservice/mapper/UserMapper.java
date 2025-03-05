package com.bohdanzhuvak.userservice.mapper;

import com.bohdanzhuvak.userservice.dto.UserResponse;
import com.bohdanzhuvak.userservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponse toResponse(User user);
}
