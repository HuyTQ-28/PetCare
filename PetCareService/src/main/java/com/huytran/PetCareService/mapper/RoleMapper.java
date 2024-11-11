package com.huytran.PetCareService.mapper;

import com.huytran.PetCareService.dto.request.RoleRequest;
import com.huytran.PetCareService.dto.response.RoleResponse;
import com.huytran.PetCareService.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
