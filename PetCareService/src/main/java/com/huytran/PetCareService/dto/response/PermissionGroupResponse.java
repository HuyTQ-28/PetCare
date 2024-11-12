package com.huytran.PetCareService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionGroupResponse {
    Long id;
    String name;
    String description;

    Set<PermissionResponse> permissions;
}
