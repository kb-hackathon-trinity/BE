package org.trinity.be.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.trinity.be.auth.type.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.trinity.be.exception.CustomException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import org.trinity.be.user.domain.User;
import org.trinity.be.user.domain.type.Role;

import static org.trinity.be.common.code.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);
    @Value("${api.jwt.secret}")
    private String secret;

    @Value("${api.jwt.domain}")
    private String domain;

    private final CustomUserDetailsService customuserDetailsService;
    private final String tokenPrefix = "Bearer ";
    private final long accessTokenTime = 30L * 60 * 1000 * 12;
    private final long refreshTokenTime = 28L * 24 * 60 * 60 * 1000;

    /**
     * Guest Access JWT 생성
     */
//    public HttpHeaders generateGuestJwt() {
//        return jwtGenerator(0L, Collections.singletonList(Role.GUEST.name()), false);
//    }

    /**
     * User Access & Refresh JWT 생성
     */
    public HttpHeaders generateUserJwt(Integer userId, List<String> roles) {
        return jwtGenerator(userId, roles, true);
    }

    /**
     * Admin Access & Refresh JWT 생성
     */
    public HttpHeaders generateAdminJwt(Integer userNo, List<String> roles) {
        return jwtGenerator(userNo, roles, true);
    }

    /**
     * Master Access JWT 생성
     */
    public HttpHeaders generateMasterJwt(Integer userNo, List<String> roles) {
        return jwtGenerator(userNo, roles, false);
    }

    /**
     * 토큰 생성자
     */
    private HttpHeaders jwtGenerator(Integer userNo, List<String> roles, boolean isRefreshToken) {
        long time = System.currentTimeMillis();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        String accessJwt = JWT.create()
                .withSubject(userNo.toString())
                .withIssuedAt(new Date(time))
                .withExpiresAt(new Date(time + accessTokenTime))
                .withIssuer(domain)
                .withClaim("roles", roles)
                .sign(algorithm);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, accessJwt);

        if (isRefreshToken) {
            String refreshJwt = JWT.create()
                    .withSubject(userNo.toString())
                    .withIssuedAt(new Date(time))
                    .withExpiresAt(new Date(time + refreshTokenTime))
                    .withIssuer(domain)
                    .withClaim("roles", roles)
                    .sign(algorithm);

            httpHeaders.add("Refresh-Token", refreshJwt);
        }

        return httpHeaders;
    }

    /**
     * JWT Guest Access 인가
     */
    public void authorizeGuestAccessJwt(String token) { authorizeJwt(token, Role.GUEST, Jwt.ACCESS); }

    /**
     * JWT user Access 인가
     */
    public User authorizeUserAccessJwt(String token) {
        return authorizeJwt(token, Role.MEMBER, Jwt.ACCESS);
    }

    /**
     * JWT user Refresh 인가
     */
    public User authorizeUserRefreshJwt(String token) {
        return authorizeJwt(token, Role.MEMBER, Jwt.REFRESH);
    }

    /**
     * JWT Admin Access 인가
     */
    public User authorizeAdminAccessJwt(String token) {
        return authorizeJwt(token, Role.ADMIN, Jwt.ACCESS);
    }

    /**
     * JWT Admin Refresh 인가
     */
//    public User authorizeAdminRefreshJwt(String token) {
//        return authorizeJwt(token, Role.ADMIN, Jwt.REFRESH);
//    }
//
//    /**
//     * JWT Master Access 인가
//     */
//    public User authorizeMasterAccessJwt(String token) {
//        return authorizeJwt(token, Role.MASTER, Jwt.ACCESS);
//    }

    /**
     * JWT 인증 |
     * 401(TOKEN_UNAUTHENTICATED)
     */
    public User authenticateJwt(String token) {
        DecodedJWT decodedJWT = jwtDecoder(token);

        Long validTime = decodedJWT.getExpiresAt().getTime() - decodedJWT.getIssuedAt().getTime();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r)));
        int userNo = Integer.parseInt(decodedJWT.getSubject());
        boolean isExpired = decodedJWT.getExpiresAt()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .isBefore(LocalDateTime.now());

        log.info(roles.toString());
        // Active member validation
        User user;
        if (userNo == 0 && roles.contains(Role.GUEST.name()))
            user = null;
//        else if (memberNum > 0L && roles.contains(Role.ADMIN.name()))
//            member = customuserDetailsService.loadAdminByUserId(memberNum);
        else if (userNo > 0L && roles.contains(Role.MEMBER.name())){

            user = customuserDetailsService.loadUserByUserNo(userNo);}
        else
            throw new CustomException(TOKEN_UNAUTHENTICATED);

        // Token time validation
        if ((!validTime.equals(accessTokenTime) && !validTime.equals(refreshTokenTime)) || isExpired)
            throw new CustomException(TOKEN_UNAUTHENTICATED);

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userNo, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (JWTVerificationException e) {
            throw new CustomException(e, TOKEN_UNAUTHENTICATED);
        }
        log.info("여기 통과하나 체크");

        return user;
    }

    /**
     * JWT 인가 |
     * 401(TOKEN_UNAUTHENTICATED)
     * 403(TOKEN_UNAUTHORIZED)
     */
    private User authorizeJwt(String token, Role role, Jwt jwt) {
        DecodedJWT decodedJWT = jwtDecoder(token);
        User user = authenticateJwt(token);

        Set<String> roles = new HashSet<>(decodedJWT.getClaim("roles").asList(String.class));
        Long validTime = decodedJWT.getExpiresAt().getTime() - decodedJWT.getIssuedAt().getTime();

        // Token valid time validation
        if (jwt.name().equals(Jwt.ACCESS.name())) {
            if (!validTime.equals(accessTokenTime))
                throw new CustomException(TOKEN_UNAUTHORIZED);
        } else if (jwt.name().equals(Jwt.REFRESH.name())) {
            if (!validTime.equals(refreshTokenTime))
                throw new CustomException(TOKEN_UNAUTHORIZED);
        } else {
            throw new CustomException(SERVER_ERROR);
        }

        // Role validation
        if (role.name().equals(Role.GUEST.name())) {
            if (!roles.contains(role.name()))
                throw new CustomException(TOKEN_UNAUTHORIZED);
        } else {
            Set<String> roleSets = new HashSet<>(user.getRoles());
            if (!roles.contains(role.name()) || !roleSets.containsAll(roles))
                throw new CustomException(TOKEN_UNAUTHORIZED);
        }

        return user;
    }

    /**
     * JWT 디코더 |
     * 401(TOKEN_UNAUTHENTICATED)
     */
    private DecodedJWT jwtDecoder(String token) {
        if (token == null || token.trim().equals(""))
            throw new CustomException(TOKEN_UNAUTHENTICATED);
        token = token.trim().substring(tokenPrefix.length());

        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new CustomException(e, TOKEN_UNAUTHENTICATED);
        }
    }
}
