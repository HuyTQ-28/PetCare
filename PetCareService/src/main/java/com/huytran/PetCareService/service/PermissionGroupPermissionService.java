package com.huytran.PetCareService.service;

import com.huytran.PetCareService.dto.request.PermissionGroupPermissionRequest;
import com.huytran.PetCareService.dto.response.PermissionGroupPermissionResponse;
import com.huytran.PetCareService.entity.Permission;
import com.huytran.PetCareService.entity.PermissionGroup;
import com.huytran.PetCareService.entity.PermissionGroupPermission;
import com.huytran.PetCareService.exception.AppException;
import com.huytran.PetCareService.exception.ErrorCode;
import com.huytran.PetCareService.mapper.PermissionGroupPermissionMapper;
import com.huytran.PetCareService.repository.PermissionGroupPermissionRepository;
import com.huytran.PetCareService.repository.PermissionGroupRepository;
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
public class PermissionGroupPermissionService {
    PermissionGroupPermissionRepository permissionGroupPermissionRepository;
    PermissionGroupPermissionMapper permissionGroupPermissionMapper;
    PermissionGroupRepository permissionGroupRepository;
    private final PermissionRepository permissionRepository;

    public PermissionGroupPermissionResponse create(PermissionGroupPermissionRequest request) {
        PermissionGroupPermission permissionGroupPermission = permissionGroupPermissionMapper.toPermissionGroupPermission(request);
        permissionGroupPermissionRepository.save(permissionGroupPermission);
        return permissionGroupPermissionMapper.toPermissionGroupPermissionResponse(permissionGroupPermission);
    }

    public List<PermissionGroupPermissionResponse> getAll() {
        return permissionGroupPermissionRepository.findAll()
                .stream()
                .map(permissionGroupPermissionMapper::toPermissionGroupPermissionResponse)
                .toList();
    }

    public PermissionGroupPermissionResponse update(Long permissionGroupPermissionId, PermissionGroupPermissionRequest request) {
        Optional<PermissionGroupPermission> optionalPermissionGroupPermission = permissionGroupPermissionRepository.findById(permissionGroupPermissionId);

        if (optionalPermissionGroupPermission.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_GROUP_PERMISSION_NOT_EXISTED);
        }

        PermissionGroupPermission permissionGroupPermission = optionalPermissionGroupPermission.get();

        // Tải đầy đủ đối tượng PermissionGroup từ cơ sở dữ liệu
        Optional<PermissionGroup> optionalPermissionGroup = permissionGroupRepository.findById(request.getPermissionGroupId());
        if (optionalPermissionGroup.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_GROUP_NOT_EXISTED);
        }
        PermissionGroup permissionGroup = optionalPermissionGroup.get();

        // Tải đầy đủ đối tượng Permission từ cơ sở dữ liệu
        Optional<Permission> optionalPermission = permissionRepository.findById(request.getPermissionId());
        if (optionalPermission.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        }
        Permission permission = optionalPermission.get();

        // Cập nhật PermisionGroup và Permission cho PermissionGroupPermission
        permissionGroupPermission.setPermissionGroup(permissionGroup);
        permissionGroupPermission.setPermission(permission);
        permissionGroupPermissionRepository.save(permissionGroupPermission);

        return permissionGroupPermissionMapper.toPermissionGroupPermissionResponse(permissionGroupPermission);
    }

    public void delete(Long permissionGroupPermissionId) {
        permissionGroupPermissionRepository.deleteById(permissionGroupPermissionId);
    }
}
