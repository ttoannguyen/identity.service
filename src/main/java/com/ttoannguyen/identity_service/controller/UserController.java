package com.ttoannguyen.identity_service.controller;

import com.ttoannguyen.identity_service.dto.ApiResponse;
import com.ttoannguyen.identity_service.dto.request.UserCreationRequest;
import com.ttoannguyen.identity_service.dto.request.UserUpdateRequest;
import com.ttoannguyen.identity_service.dto.response.UserResponse;
import com.ttoannguyen.identity_service.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
  UserService userService;

  @PostMapping
  ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
    return ApiResponse.<UserResponse>builder().result(userService.createRequest(request)).build();
  }

  @GetMapping
  List<UserResponse> getUsers() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("Username: {}", authentication.getName());
    authentication.getAuthorities().forEach(t -> log.info(t.getAuthority()));
    return userService.getUsers();
  }

  @GetMapping("/{userId}")
  ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
    return ApiResponse.<UserResponse>builder().result(userService.getUser(userId)).build();
  }

  @GetMapping("/myInfo")
  ApiResponse<UserResponse> getMyInfo() {
    return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
  }

  @PutMapping("/{userId}")
  UserResponse updateUser(
      @RequestBody UserUpdateRequest request, @PathVariable("userId") String userId) {
    return userService.updateUser(request, userId);
  }

  @DeleteMapping("/{userId}")
  String deleteUser(@PathVariable String userId) {
    userService.deleteUser(userId);
    return "User has been deleted";
  }
}
