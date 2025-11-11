package com.ttoannguyen.identity_service.service;

import com.ttoannguyen.identity_service.dto.request.PermissionRequest;
import com.ttoannguyen.identity_service.dto.request.RoleRequest;
import com.ttoannguyen.identity_service.dto.response.PermissionResponse;
import com.ttoannguyen.identity_service.dto.response.RoleResponse;
import com.ttoannguyen.identity_service.entity.Permission;
import com.ttoannguyen.identity_service.entity.Role;
import com.ttoannguyen.identity_service.mapper.PermissionMapper;
import com.ttoannguyen.identity_service.mapper.RoleMapper;
import com.ttoannguyen.identity_service.repository.PermissionRepository;
import com.ttoannguyen.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request){
        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);

    }

    public List<RoleResponse> getAllRole(){
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String roleName){
        roleRepository.deleteById(roleName);
    }

}
