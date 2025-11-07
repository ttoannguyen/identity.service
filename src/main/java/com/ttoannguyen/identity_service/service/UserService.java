package com.ttoannguyen.identity_service.service;

import com.ttoannguyen.identity_service.dto.request.UserCreationRequest;
import com.ttoannguyen.identity_service.dto.request.UserUpdateRequest;
import com.ttoannguyen.identity_service.dto.response.UserResponse;
import com.ttoannguyen.identity_service.entity.User;
import com.ttoannguyen.identity_service.enums.Role;
import com.ttoannguyen.identity_service.exception.AppException;
import com.ttoannguyen.identity_service.exception.ErrorCode;
import com.ttoannguyen.identity_service.mapper.UserMapper;
import com.ttoannguyen.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
  UserRepository userRepository;
  UserMapper userMapper;

  public UserResponse createRequest(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername()))
      throw new AppException(ErrorCode.USER_EXISTED);
    User user = userMapper.toUser(request);

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    HashSet<String> roles = new HashSet<>();
    roles.add(Role.USER.name());
    user.setRoles(roles);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getUsers() {
    log.info("Get all user");
    return userMapper.toListUserResponses(userRepository.findAll());
  }

  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse getUser(String userId) {
    log.info(SecurityContextHolder.getContext().getAuthentication().getName());
    return userMapper.toUserResponse(
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
  }

  public UserResponse getMyInfo() {
    return userMapper.toUserResponse(
        userRepository
            .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
  }

  public UserResponse updateUser(UserUpdateRequest request, String userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    return userMapper.toUserResponse(userRepository.save(userMapper.updateUser(user, request)));
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }
}
