package com.huytran.PetCareService.service;

import com.huytran.PetCareService.dto.request.RolePermissionGroupRequest;
import com.huytran.PetCareService.dto.response.RolePermissionGroupResponse;
import com.huytran.PetCareService.entity.PermissionGroup;
import com.huytran.PetCareService.entity.Role;
import com.huytran.PetCareService.entity.RolePermissionGroup;
import com.huytran.PetCareService.exception.AppException;
import com.huytran.PetCareService.exception.ErrorCode;
import com.huytran.PetCareService.mapper.RolePermissionGroupMapper;
import com.huytran.PetCareService.repository.PermissionGroupRepository;
import com.huytran.PetCareService.repository.RolePermissionGroupRepository;
import com.huytran.PetCareService.repository.RoleRepository;
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
public class RolePermissionGroupService {
    RolePermissionGroupRepository rolePermissionGroupRepository;
    RolePermissionGroupMapper rolePermissionGroupMapper;
    private final PermissionGroupRepository permissionGroupRepository;
    private final RoleRepository roleRepository;

    public RolePermissionGroupResponse create(RolePermissionGroupRequest request) {
        RolePermissionGroup rolePermissionGroup = rolePermissionGroupMapper.toRolePermissionGroup(request);
        rolePermissionGroupRepository.save(rolePermissionGroup);
        return rolePermissionGroupMapper.toRolePermissionGroupResponse(rolePermissionGroup);
    }

    public List<RolePermissionGroupResponse> getAll() {
        return rolePermissionGroupRepository.findAll()
                .stream()
                .map(rolePermissionGroupMapper::toRolePermissionGroupResponse)
                .toList();
    }

    public RolePermissionGroupResponse update(Long rolePermissionGroupId, RolePermissionGroupRequest request) {
        Optional<RolePermissionGroup> optionalRolePermissionGroup = rolePermissionGroupRepository.findById(rolePermissionGroupId);

        if (optionalRolePermissionGroup.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_PERMISSION_GROUP_NOT_EXISTED);
        }

        RolePermissionGroup rolePermissionGroup = optionalRolePermissionGroup.get();

        // Tải đầy đủ đối tượng PermissionGroup từ cơ sở dữ liệu
        Optional<PermissionGroup> optionalPermissionGroup = permissionGroupRepository.findById(request.getPermissionGroupId());
        if (optionalPermissionGroup.isEmpty()) {
            throw new AppException(ErrorCode.PERMISSION_GROUP_NOT_EXISTED);
        }
        PermissionGroup permissionGroup = optionalPermissionGroup.get();

        Optional<Role> optionalRole = roleRepository.findById(request.getRoleId());
        if (optionalRole.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }
        Role role = optionalRole.get();

        // Gán giá trị mới
        rolePermissionGroup.setPermissionGroup(permissionGroup);
        rolePermissionGroup.setRole(role);
        rolePermissionGroupRepository.save(rolePermissionGroup);

        return rolePermissionGroupMapper.toRolePermissionGroupResponse(rolePermissionGroup);

    }

    public void delete(Long rolePermissionGroupId) {
        rolePermissionGroupRepository.deleteById(rolePermissionGroupId);
    }
}
