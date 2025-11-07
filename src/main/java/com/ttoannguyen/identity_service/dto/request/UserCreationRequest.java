package com.ttoannguyen.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
  @Size(min = 3, message = "USERNAME_INVALID")
  String username;

  @Size(min = 8, message = "INVALID_PASSWORD")
  String password;

  String firstname;
  String lastname;
  LocalDate dob;
}
