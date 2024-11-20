package org.trinity.be.user.mapper;


import org.trinity.be.user.domain.User;

public interface UserMapper {

    User findOneByUserID(String id);
    User findOneByUserNo(int id);


}
