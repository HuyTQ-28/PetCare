package com.huytran.PetCareService.service;

import com.huytran.PetCareService.dto.request.LogoutRequest;
import com.huytran.PetCareService.dto.request.RefreshRequest;
import com.huytran.PetCareService.entity.*;
import com.huytran.PetCareService.dto.request.AuthenticationRequest;
import com.huytran.PetCareService.dto.request.IntrospectRequest;
import com.huytran.PetCareService.dto.response.AuthenticationResponse;
import com.huytran.PetCareService.dto.response.IntrospectResponse;
import com.huytran.PetCareService.exception.AppException;
import com.huytran.PetCareService.exception.ErrorCode;
import com.huytran.PetCareService.repository.InvalidatedTokenRepository;
import com.huytran.PetCareService.repository.UserRepository;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    // Ktra xem token có hợp lệ không
    public IntrospectResponse instrospsect(IntrospectRequest request)
            throws JOSEException, ParseException {

        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    // Xác thực cho user -> trả về token để đăng nhập
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // Lấy thông tin của user qua username
        var user = userRepository.findByUsername((request.getUsername()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra xem mật khẩu từ request truyền vào có match với dữ liệu trên DB hay không
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        // Lấy token
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request)
            throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            // Lấy jwt token id ra
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            log.info("Token already expired");
        }
    }

    //
    private SignedJWT verifyToken(String token, boolean isRefresh)
            throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        // Lấy tgian để xem token hết hạn hay chưa
        Date expiryTime = (isRefresh) // Nếu isRefresh = true thì thực hiện verify refresh, ng lại thì authenticate
                ? new Date(signedJWT
                    .getJWTClaimsSet()
                    .getIssueTime()
                    .toInstant()
                    .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                    .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        // Nếu token kh verify hoặc hết hạn thì throw exception
        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Kiểm tra xem token có trong bảng invalidated_token hay không
        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
                    throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);
        // Lấy id và tgian hết hạn của token
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
        // Đưa token vào bảng invalidated_token
        invalidatedTokenRepository.save(invalidatedToken);
        // issue 1 token mới
        var username = signedJWT.getJWTClaimsSet().getSubject();
        // Tim user
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        // generate token dựa vào thông tin user
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private String generateToken(User user) {
        // Thuật toán mã hóa
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        // Data trong body gọi là claim (bên trong gồm các thông tin có trong token)
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("huytran.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString()) // Lưu token id
                .claim("scope", buildScope(user)) // scope chứa thông tin về role và permission
                .build();
        // Tạo payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        // Ký token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    // Scope chứa thông tin về role và các quyền trong đó
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

//        if (!CollectionUtils.isEmpty(user.getUserRoles()))
//            user.getUserRoles().forEach(userRole -> {
//                Role role = userRole.getRole();
//                stringJoiner.add("ROLE_" + role.getName());
//
//                if (!CollectionUtils.isEmpty(role.getRolePermissionGroups())) {
//                    role.getRolePermissionGroups().forEach(rolePermissionGroup -> {
//                        PermissionGroup permissionGroup = rolePermissionGroup.getPermissionGroup();
//
//                        if (!CollectionUtils.isEmpty(permissionGroup.getPermissionGroupPermissions())) {
//                            permissionGroup.getPermissionGroupPermissions().forEach(permissionGroupPermission -> {
//                                Permission permission = permissionGroupPermission.getPermission();
//                                stringJoiner.add(permission.getName());
//                            });
//                        }
//                    });
//                }
//            });

        return  stringJoiner.toString();
    }
}
