package com.ttoannguyen.identity_service.configuration;

import com.ttoannguyen.identity_service.entity.User;
import com.ttoannguyen.identity_service.enums.Role;
import com.ttoannguyen.identity_service.repository.UserRepository;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

  private PasswordEncoder passwordEncoder;

  @Bean
  ApplicationRunner applicationRunner(UserRepository userRepository) {
    return args -> {
      if (userRepository.findByUsername("admin").isEmpty()) {
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.ADMIN.name());
        User user =
            User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
//                .roles(roles)
                .build();
        userRepository.save(user);
        log.warn("Admin user has been created with default password: admin, please change it!");
      }
    };
  }
}
