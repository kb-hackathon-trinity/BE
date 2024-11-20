package org.trinity.be.user.service;


import org.trinity.be.user.domain.User;
import org.trinity.be.user.dto.req.UserLoginReqDto;

public interface UserService {

    public User login(UserLoginReqDto userLoginReqDto);


}
