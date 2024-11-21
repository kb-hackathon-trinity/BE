package org.trinity.be.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.trinity.be.exception.CustomException;
import org.trinity.be.user.domain.User;
import org.trinity.be.user.domain.UserRole;
import org.trinity.be.user.dto.req.UserLoginReqDto;
import org.trinity.be.user.mapper.UserMapper;
import org.trinity.be.user.mapper.UserRoleMapper;

import java.util.List;
import java.util.Optional;

import static org.trinity.be.common.code.ErrorCode.LOGIN_UNAUTHENTICATED;
import static org.trinity.be.common.code.ErrorCode.SERVER_ERROR;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    @Bean
    private PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}


    public User login(UserLoginReqDto userLoginReqDto) {
        User user = userMapper.findOneByUserID(userLoginReqDto.getId());
        log.info("user 찾기 성공");
        log.info(user.getName());

        if (verifyPassword(user, userLoginReqDto.getPw())) {
            throw new CustomException(LOGIN_UNAUTHENTICATED);
        }
        user.addMemberRole(getUserRolesByUserNo(user.getUserNo()));
        log.info("역할 추가 완료");
        return user;
    }


    private boolean verifyPassword(User user, String pw) {
        // 로그인 시 비밀번호 일치여부 확인
        log.info("user.getPassword = " + user.getPassword());
        log.info("pw = " + pw);
        return passwordEncoder().matches(pw, user.getPassword());

    }

    private User findUserByID(String id) {
        Optional<User> member = Optional.ofNullable(userMapper.findOneByUserID(id));

        if (member.isEmpty()) {
            log.info("사용자가 존재하지 않습니다.");
            throw new CustomException(LOGIN_UNAUTHENTICATED);
        }

        return member.get();
    }

    private List<UserRole> getUserRolesByUserNo(int userNo) {
        try {
            return userRoleMapper.findUserRoleByUserNo(userNo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
