package com.huytran.PetCareService.controller;

import com.huytran.PetCareService.dto.request.RolePermissionGroupRequest;
import com.huytran.PetCareService.dto.response.ApiResponse;
import com.huytran.PetCareService.dto.response.RolePermissionGroupResponse;
import com.huytran.PetCareService.service.RolePermissionGroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role_permission_group")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RolePermissionGroupController {
    RolePermissionGroupService rolePermissionGroupService;

    @PostMapping
    ApiResponse<RolePermissionGroupResponse> create(@RequestBody RolePermissionGroupRequest request) {
        return ApiResponse.<RolePermissionGroupResponse>builder()
                .result(rolePermissionGroupService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RolePermissionGroupResponse>> getAll() {
        return ApiResponse.<List<RolePermissionGroupResponse>>builder()
                .result(rolePermissionGroupService.getAll())
                .build();
    }

    @DeleteMapping("/{rolePermissionGroupId}")
    ApiResponse<Void> delete(@PathVariable Long rolePermissionGroupId) {
        rolePermissionGroupService.delete(rolePermissionGroupId);
        return ApiResponse.<Void>builder().build();
    }
}
