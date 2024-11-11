package com.huytran.PetCareService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Long roleId;
    String name;
    String description;
    Set<PermissionGroupResponse> permissions;
}
