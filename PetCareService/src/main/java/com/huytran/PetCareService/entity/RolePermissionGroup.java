package com.huytran.PetCareService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class RolePermissionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "permission_group_id", nullable = false)
    PermissionGroup permissionGroup;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;
}
