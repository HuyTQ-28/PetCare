package com.huytran.PetCareService.service;

import com.huytran.PetCareService.dto.request.PermissionGroupRequest;
import com.huytran.PetCareService.dto.response.PermissionGroupResponse;
import com.huytran.PetCareService.entity.PermissionGroup;
import com.huytran.PetCareService.exception.AppException;
import com.huytran.PetCareService.exception.ErrorCode;
import com.huytran.PetCareService.mapper.PermissionGroupMapper;
import com.huytran.PetCareService.repository.PermissionGroupRepository;

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
public class PermissionGroupService {
    PermissionGroupRepository permissionGroupRepository;
    PermissionGroupMapper permissionGroupMapper;

    public PermissionGroupResponse create(PermissionGroupRequest request) {
        PermissionGroup permissionGroup = permissionGroupMapper.toPermissionGroup(request);
        permissionGroup = permissionGroupRepository.save(permissionGroup);
        return permissionGroupMapper.toPermissionGroupResponse(permissionGroup);
    }

    public List<PermissionGroupResponse> getAll() {
        return permissionGroupRepository.findAll()
                .stream()
                .map(permissionGroupMapper::toPermissionGroupResponse)
                .toList();
    }

    public PermissionGroupResponse updatePermissionGroup(Long permissionGroupId, PermissionGroupRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        // Tìm Permission Group theo id
        Optional<PermissionGroup> optionalPermissionGroup = permissionGroupRepository.findById(permissionGroupId);

        // Nếu không tìm thấy, throw exception
        if (optionalPermissionGroup.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_GROUP_NOT_EXISTED);
        }

        PermissionGroup permissionGroup = optionalPermissionGroup.get();

        // Cập nhật thông tin
        permissionGroup.setName(request.getName());
        permissionGroup.setDescription(request.getDescription());

        PermissionGroup updatedPermissionGroup = permissionGroupRepository.save(permissionGroup);

        return permissionGroupMapper.toPermissionGroupResponse(updatedPermissionGroup);
    }

    public void delete(Long permissionGroupId) {
        permissionGroupRepository.deleteById(permissionGroupId);
    }
}
