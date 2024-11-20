package org.trinity.be.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 400
     */
    PASSWORD_MATCH_INVALID(BAD_REQUEST, "비밀번호와 비밀번호 재입력이 동일하지 않습니다."),
    PASSWORD_INVALID(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    /**
     * 401 Unauthorized
     */
    LOGIN_UNAUTHENTICATED(UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다"),
    TOKEN_UNAUTHENTICATED(UNAUTHORIZED, "승인되지 않은 요청입니다. 로그인 후에 다시 시도 해주세요."),

    /**
     * 403 Forbidden
     */
    TOKEN_UNAUTHORIZED(FORBIDDEN, "권한이 없는 요청입니다. 로그인 후에 다시 시도 해주세요."),
    REQUEST_FORBIDDEN(FORBIDDEN, "권한이 없는 요청입니다."),

    /**
     * 404 Not found
     */
    MEMBER_NOT_FOUND(NOT_FOUND, "존재하지 않는 회원입니다."),

    /**
     * 409 Conflict
     */
    EXISTING_MEMBER_ID(CONFLICT, "이미 사용중인 ID입니다."),
    EXISTING_EMAIL(CONFLICT, "이미 사용중인 이메일입니다."),
    EXISTING_CART_ITEM(CONFLICT, "이미 장바구니에 등록된 상품입니다."),

    /**
     * 500 Internal server error
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");



    private final HttpStatus httpStatus;
    private final String message;
}
