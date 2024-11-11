package com.huytran.PetCareService.mapper;

import com.huytran.PetCareService.dto.request.UserCreationRequest;
import com.huytran.PetCareService.dto.request.UserUpdateRequest;
import com.huytran.PetCareService.dto.response.UserResponse;
import com.huytran.PetCareService.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
