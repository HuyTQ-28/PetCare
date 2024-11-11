package com.huytran.PetCareService.controller;

import com.huytran.PetCareService.dto.response.ApiResponse;
import com.huytran.PetCareService.dto.request.PermissionRequest;
import com.huytran.PetCareService.dto.response.PermissionResponse;
import com.huytran.PetCareService.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @PutMapping("/{permissionId}")
    ApiResponse<PermissionResponse> update(@PathVariable Long permissionId, @RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.updatePermission(permissionId, request.getName(), request.getDescription()))
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> delete(@PathVariable Long permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
