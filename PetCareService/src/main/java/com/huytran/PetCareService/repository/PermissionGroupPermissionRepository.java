package com.huytran.PetCareService.repository;

import com.huytran.PetCareService.entity.PermissionGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionGroupPermissionRepository extends JpaRepository<PermissionGroupPermission, Long> {
}