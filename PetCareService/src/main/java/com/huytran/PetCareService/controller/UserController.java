package com.huytran.PetCareService.controller;

import com.huytran.PetCareService.dto.response.ApiResponse;
import com.huytran.PetCareService.dto.request.UserCreationRequest;
import com.huytran.PetCareService.dto.request.UserUpdateRequest;
import com.huytran.PetCareService.dto.response.UserResponse;
import com.huytran.PetCareService.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    // Tạo user, trong đó đc gán sẵn role là CUSTOMER
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
    // Lấy thông tin của tất cả Users (chỉ có ADMIN)
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }
    // Lấy thông tin của User bằng ID (chỉ có ADMIN)
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }
    // Lấy thông tin của cá nhân User (bằng Token)
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
    // Cập nhật thông tin User qua ID (password, firstName, lastName, dob)
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
    // Xóa User qua ID
    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser (@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }
}
