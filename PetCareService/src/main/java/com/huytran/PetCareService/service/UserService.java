package com.huytran.PetCareService.service;

import com.huytran.PetCareService.constant.PredefinedRole;
import com.huytran.PetCareService.dto.request.UserCreationRequest;
import com.huytran.PetCareService.dto.request.UserUpdateRequest;
import com.huytran.PetCareService.dto.response.UserResponse;
import com.huytran.PetCareService.entity.Role;
import com.huytran.PetCareService.entity.User;
import com.huytran.PetCareService.entity.UserRole;
import com.huytran.PetCareService.exception.AppException;
import com.huytran.PetCareService.exception.ErrorCode;
import com.huytran.PetCareService.mapper.UserMapper;
import com.huytran.PetCareService.repository.RoleRepository;
import com.huytran.PetCareService.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (request.getDob() == null) {
            user.setDob(LocalDate.now()); // Hoặc một giá trị mặc định khác
        }

        // Lấy vai trò "CUSTOMER" từ RoleRepository
        Role userRole = roleRepository.findByName(PredefinedRole.CUSTOMER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        // Tạo đối tượng UserRole và gán cho người dùng
        UserRole userRoleEntity = UserRole.builder()
                .user(user)
                .role(userRole)
                .build();

        user.setUserRoles(Set.of(userRoleEntity));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

//    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Update roles
        Set<UserRole> userRoles = request.getRoles().stream()
                .map(roleName -> {
                    Role role = roleRepository.findByName(roleName)
                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                    return new UserRole(user, role);
                })
                .collect(Collectors.toSet());
        user.setUserRoles(userRoles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
