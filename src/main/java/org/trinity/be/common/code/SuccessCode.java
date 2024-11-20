package org.trinity.be.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum SuccessCode {


    /**
     * User Controller
     */
    // 200 OK
    USERNAME_AVAILABLE(OK, "아이디 중복 확인을 했습니다."),
    EMAIL_AVAILABLE(OK, "이메일 중복 확인을 했습니다."),
    SELF_USER_FOUND(OK, "본인을 조회 했습니다."),
    USER_LOGOUT(OK, "회원이 로그아웃 했습니다."),
    USER_LOGIN(OK, "회원이 로그인 했습니다."),
    PASSWORD_UPDATED(OK, "비밀번호를 업데이트 했습니다."),
    ADD_CART_ITEM(OK, "장바구니에 상품을 추가했습니다."),

    //201
    USER_REGISTERED(CREATED, "회원 가입을 했습니다.");



    private final HttpStatus httpStatus;
    private final String message;
}
