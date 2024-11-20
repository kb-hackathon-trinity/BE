package org.trinity.be.auth;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.trinity.be.user.domain.User;

import javax.servlet.http.HttpServletRequest;

@Component
@Log4j
public class JwtUtils {
    private final JwtProvider jwtProvider;

    public JwtUtils(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    // 토큰에서 memberNum 가져오기
    public long extractMemberNum(HttpServletRequest request) {
        String token = request.getHeader("Refresh-Token") == null ?
                request.getHeader(HttpHeaders.AUTHORIZATION) :
                request.getHeader("Refresh-Token");

        User user = jwtProvider.authenticateJwt(token);
        return user.getUserNo(); // 사용자 번호 추출
    }

}
