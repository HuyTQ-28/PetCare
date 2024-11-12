package com.huytran.PetCareService.repository;

import com.huytran.PetCareService.entity.PermissionGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionGroupPermissionRepository extends JpaRepository<PermissionGroupPermission, Long> {
}