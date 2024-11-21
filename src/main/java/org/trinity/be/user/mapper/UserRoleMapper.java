package org.trinity.be.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.trinity.be.user.domain.UserRole;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    int save(UserRole userRoleDto);
    List<UserRole> findUserRoleByUserNo(int userNo);
}
