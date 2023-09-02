package com.shop.exception.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotExistMemberException extends RuntimeException {
    public NotExistMemberException(String msg) {
        super(msg);
    }

}
