package com.huytran.PetCareService.controller;

import com.huytran.PetCareService.dto.response.ApiResponse;
import com.huytran.PetCareService.dto.request.RoleRequest;
import com.huytran.PetCareService.dto.response.RoleResponse;
import com.huytran.PetCareService.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @PostMapping("/{roleId}")
    ApiResponse<RoleResponse> update(@PathVariable Long roleId, @RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(roleId, request.getName(), request.getDescription()))
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> delete(@PathVariable Long roleId) {
        roleService.delete(roleId);
        return ApiResponse.<Void>builder().build();
    }
}
