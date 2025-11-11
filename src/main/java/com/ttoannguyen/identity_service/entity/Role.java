package com.ttoannguyen.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "Roles")
public class Role {
      @Id
      String name;
      String description;

      @ManyToMany
      Set<Permission> permissions;
}
