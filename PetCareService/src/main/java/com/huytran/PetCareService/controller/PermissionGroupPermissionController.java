package com.huytran.PetCareService.controller;

import com.huytran.PetCareService.dto.request.PermissionGroupPermissionRequest;
import com.huytran.PetCareService.dto.response.ApiResponse;
import com.huytran.PetCareService.dto.response.PermissionGroupPermissionResponse;
import com.huytran.PetCareService.service.PermissionGroupPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permission_group_permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionGroupPermissionController {
    PermissionGroupPermissionService permissionGroupPermissionService;

    @PostMapping
    ApiResponse<PermissionGroupPermissionResponse> create(@RequestBody PermissionGroupPermissionRequest request) {
        return ApiResponse.<PermissionGroupPermissionResponse>builder()
                .result(permissionGroupPermissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionGroupPermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionGroupPermissionResponse>>builder()
                .result(permissionGroupPermissionService.getAll())
                .build();
    }

    @PostMapping("/{permissionGroupPermissionId}")
    ApiResponse<PermissionGroupPermissionResponse> update(@PathVariable Long permissionGroupPermissionId, @RequestBody PermissionGroupPermissionRequest request) {
        return ApiResponse.<PermissionGroupPermissionResponse>builder()
                .result(permissionGroupPermissionService.update(permissionGroupPermissionId, request))
                .build();
    }

    @DeleteMapping("/{permissionGroupPermissionId}")
    ApiResponse<Void> delete(@PathVariable Long permissionGroupPermissionId) {
        permissionGroupPermissionService.delete(permissionGroupPermissionId);
        return ApiResponse.<Void>builder().build();
    }
}
