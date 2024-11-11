package com.huytran.PetCareService.service;

import com.huytran.PetCareService.dto.request.RoleRequest;
import com.huytran.PetCareService.dto.response.RoleResponse;
import com.huytran.PetCareService.entity.Role;
import com.huytran.PetCareService.exception.AppException;
import com.huytran.PetCareService.exception.ErrorCode;
import com.huytran.PetCareService.mapper.RoleMapper;
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
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public RoleResponse updateRole(Long roleId, String name, String description) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);

        if (optionalRole.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }

        Role role = optionalRole.get();

        role.setName(name);
        role.setDescription(description);

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public void delete(Long roleId) {
        roleRepository.deleteById(roleId);
    }
}