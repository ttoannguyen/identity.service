package com.ttoannguyen.identity_service.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String username;
  String password;
  String firstname;
  String lastname;
  LocalDate dob;

    @ManyToMany
  Set<Role> roles;
}
