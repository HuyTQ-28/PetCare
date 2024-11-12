package com.huytran.PetCareService.repository;

import com.huytran.PetCareService.entity.RolePermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionGroupRepository extends JpaRepository<RolePermissionGroup, Long> {
}