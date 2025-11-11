package com.ttoannguyen.identity_service.mapper;

import com.ttoannguyen.identity_service.dto.request.UserCreationRequest;
import com.ttoannguyen.identity_service.dto.request.UserUpdateRequest;
import com.ttoannguyen.identity_service.dto.response.UserResponse;
import com.ttoannguyen.identity_service.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roles", ignore = true)
  User toUser(UserCreationRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "roles", ignore = true)
  User updateUser(@MappingTarget User user, UserUpdateRequest request);

  @Mapping(target = "roles", ignore = true)
  UserResponse toUserResponse(User user);

  List<UserResponse> toListUserResponses(List<User> users);
}
