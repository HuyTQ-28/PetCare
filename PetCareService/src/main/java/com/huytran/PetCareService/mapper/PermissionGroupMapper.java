package com.huytran.PetCareService.mapper;

import com.huytran.PetCareService.dto.request.PermissionGroupRequest;
import com.huytran.PetCareService.dto.request.PermissionRequest;
import com.huytran.PetCareService.dto.response.PermissionGroupResponse;
import com.huytran.PetCareService.dto.response.PermissionResponse;
import com.huytran.PetCareService.entity.Permission;
import com.huytran.PetCareService.entity.PermissionGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionGroupMapper {
    PermissionGroup toPermissionGroup(PermissionGroupRequest request);

    PermissionGroupResponse toPermissionGroupResponse(PermissionGroup permissionGroup);
}
