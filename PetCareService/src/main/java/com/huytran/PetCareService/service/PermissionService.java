package com.huytran.PetCareService.service;

import com.huytran.PetCareService.dto.request.PermissionRequest;
import com.huytran.PetCareService.dto.response.PermissionResponse;
import com.huytran.PetCareService.entity.Permission;
import com.huytran.PetCareService.exception.AppException;
import com.huytran.PetCareService.exception.ErrorCode;
import com.huytran.PetCareService.mapper.PermissionMapper;
import com.huytran.PetCareService.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public  PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public PermissionResponse updatePermission(Long permissionId, String name, String description) {
        // Tìm quyền truy cập theo ID
        Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);

        // Nếu không tìm thấy quyền truy cập, throw exception
        if (optionalPermission.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        }

        Permission permission = optionalPermission.get();

        // Cập nhật thông tin
        permission.setName(name);
        permission.setDescription(description);

        // Lưu lại vào database
        Permission updatedPermission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(updatedPermission);
    }

    public void delete(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
