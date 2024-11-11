package com.huytran.PetCareService.mapper;

import com.huytran.PetCareService.dto.request.PermissionRequest;
import com.huytran.PetCareService.dto.response.PermissionResponse;
import com.huytran.PetCareService.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
