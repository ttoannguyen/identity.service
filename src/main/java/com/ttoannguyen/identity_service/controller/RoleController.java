package com.ttoannguyen.identity_service.controller;

import com.ttoannguyen.identity_service.dto.ApiResponse;
import com.ttoannguyen.identity_service.dto.request.PermissionRequest;
import com.ttoannguyen.identity_service.dto.request.RoleRequest;
import com.ttoannguyen.identity_service.dto.response.PermissionResponse;
import com.ttoannguyen.identity_service.dto.response.RoleResponse;
import com.ttoannguyen.identity_service.service.PermissionService;
import com.ttoannguyen.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request)).build();
    }


    @GetMapping
    ApiResponse<List<RoleResponse>> getAllPermissions(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRole()).build();
    }


    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission){
        roleService.delete(permission);
        return ApiResponse.<Void>builder()
                .build();
    }
}
