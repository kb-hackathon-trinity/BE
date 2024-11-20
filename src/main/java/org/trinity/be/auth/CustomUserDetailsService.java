package org.trinity.be.auth;

import org.trinity.be.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.trinity.be.user.domain.User;
import org.trinity.be.user.mapper.UserMapper;
import org.trinity.be.user.mapper.UserRoleMapper;

import static org.trinity.be.common.code.ErrorCode.MEMBER_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    // ID로 조회
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userMapper.findOneByUserID(id);
        if (user == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getAuthorities());
    }

    public User loadUserByUserNo(Integer userNo) {
        User user = userMapper.findOneByUserNo(userNo);
        log.info(user.toString());
        if (user == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        user.addMemberRole(userRoleMapper.findUserRoleByUserNo(userNo));

        return user;
    }


}
