package org.trinity.be.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Refresh-Token") == null ?
                request.getHeader(AUTHORIZATION) : request.getHeader("Refresh-Token");

        if (token != null && token.startsWith("Bearer")) {
            try {
                jwtProvider.authenticateJwt(token);
            } catch (Exception e) {
                // 예외 로깅 및 401 응답 반환
                logger.error("JWT 인증 실패: " + e.getMessage());
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
