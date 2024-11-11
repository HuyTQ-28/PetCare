package com.huytran.PetCareService.configuration;

import com.huytran.PetCareService.constant.PredefinedRole;
import com.huytran.PetCareService.entity.Role;
import com.huytran.PetCareService.entity.User;
import com.huytran.PetCareService.entity.UserRole;
import com.huytran.PetCareService.repository.RoleRepository;
import com.huytran.PetCareService.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    // Tạo tài khoản admin nếu chưa có
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(PredefinedRole.ADMIN_ROLE).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.CUSTOMER_ROLE)
                        .description("Customer role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .firstName("Admin")
                        .lastName("Admin")
                        .dob(LocalDate.now())
                        .build();

                // Tạo đối tượng UserRole và gán cho người dùng
                UserRole userRole = UserRole.builder()
                        .user(user)
                        .role(adminRole)
                        .build();

                user.setUserRoles(Set.of(userRole));

                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin. Please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
