package com.huytran.PetCareService.mapper;

import com.huytran.PetCareService.dto.request.RolePermissionGroupRequest;
import com.huytran.PetCareService.dto.response.RolePermissionGroupResponse;
import com.huytran.PetCareService.entity.RolePermissionGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionGroupMapper {
    RolePermissionGroup toRolePermissionGroup(RolePermissionGroupRequest request);

    RolePermissionGroupResponse toRolePermissionGroupResponse(RolePermissionGroup rolePermissionGroup);
}
