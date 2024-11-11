package com.huytran.PetCareService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionGroupPermissionResponse {
    Long id;
    Long permissionGroupId;
    Long permissionId;
}