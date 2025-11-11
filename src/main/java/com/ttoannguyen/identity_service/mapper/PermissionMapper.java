package com.ttoannguyen.identity_service.mapper;

import com.ttoannguyen.identity_service.dto.request.PermissionRequest;
import com.ttoannguyen.identity_service.dto.request.UserCreationRequest;
import com.ttoannguyen.identity_service.dto.request.UserUpdateRequest;
import com.ttoannguyen.identity_service.dto.response.PermissionResponse;
import com.ttoannguyen.identity_service.dto.response.UserResponse;
import com.ttoannguyen.identity_service.entity.Permission;
import com.ttoannguyen.identity_service.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);

  PermissionResponse toPermissionResponse(Permission permission);
}
