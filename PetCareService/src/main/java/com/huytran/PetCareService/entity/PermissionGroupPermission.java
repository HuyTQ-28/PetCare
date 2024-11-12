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
public class PermissionGroupPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "permission_group_id")
    PermissionGroup permissionGroup;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    Permission permission;
}
