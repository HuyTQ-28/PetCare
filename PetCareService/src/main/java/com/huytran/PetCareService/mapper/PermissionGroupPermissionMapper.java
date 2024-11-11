package com.huytran.PetCareService.mapper;

import com.huytran.PetCareService.dto.request.PermissionGroupPermissionRequest;
import com.huytran.PetCareService.dto.response.PermissionGroupPermissionResponse;
import com.huytran.PetCareService.entity.PermissionGroupPermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionGroupPermissionMapper {
    PermissionGroupPermission toPermissionGroupPermission(PermissionGroupPermissionRequest request);

    PermissionGroupPermissionResponse toPermissionGroupPermissionResponse(PermissionGroupPermission permissionGroupPermission);
}