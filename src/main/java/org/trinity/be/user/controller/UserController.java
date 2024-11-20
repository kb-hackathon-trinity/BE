package org.trinity.be.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.trinity.be.auth.JwtProvider;
import org.trinity.be.auth.JwtUtils;
import org.trinity.be.common.dto.DefaultResDto;
import org.trinity.be.user.domain.User;
import org.trinity.be.user.dto.req.UserLoginReqDto;
import org.trinity.be.user.dto.res.UserDefaultResDto;
import org.trinity.be.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static org.trinity.be.common.code.SuccessCode.USER_LOGIN;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<DefaultResDto<Object>> login(@RequestBody UserLoginReqDto userLoginReqDto) {
        User user = userService.login(userLoginReqDto);

        HttpHeaders headers = jwtProvider.generateUserJwt(user.getUserNo(), user.getRoles());
        UserDefaultResDto response = new UserDefaultResDto(user);

        log.info(headers.toString());

        log.info(response.toString());
        return ResponseEntity.status(USER_LOGIN.getHttpStatus())
                .headers(headers)
                .body(DefaultResDto.singleDataBuilder()
                        .responseCode(USER_LOGIN.name())
                        .responseMessage(USER_LOGIN.getMessage())
                        .data(response)
                        .build());
    }
}
