package com.huytran.PetCareService.controller;

import com.huytran.PetCareService.dto.request.PermissionGroupRequest;
import com.huytran.PetCareService.dto.response.ApiResponse;
import com.huytran.PetCareService.dto.response.PermissionGroupResponse;
import com.huytran.PetCareService.service.PermissionGroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permission_groups")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionGroupController {
    PermissionGroupService permissionGroupService;

    @PostMapping
    ApiResponse<PermissionGroupResponse> create(@RequestBody PermissionGroupRequest request) {
        return ApiResponse.<PermissionGroupResponse>builder()
                .result(permissionGroupService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionGroupResponse>> getAll() {
        return ApiResponse.<List<PermissionGroupResponse>>builder()
                .result(permissionGroupService.getAll())
                .build();
    }

    @PutMapping("{permissionGroupId}")
    ApiResponse<PermissionGroupResponse> update(@PathVariable Long permissionGroupId, @RequestBody PermissionGroupRequest request) {
        return ApiResponse.<PermissionGroupResponse>builder()
                .result(permissionGroupService.updatePermissionGroup(permissionGroupId, request))
                .build();
    }

    @DeleteMapping("/{permissionGroupId}")
    ApiResponse<Void> delete(@PathVariable Long permissionGroupId) {
        permissionGroupService.delete(permissionGroupId);
        return ApiResponse.<Void>builder().build();
    }
}
