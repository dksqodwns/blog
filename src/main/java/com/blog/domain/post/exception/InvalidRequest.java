package com.blog.domain.post.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends BlogException {

    public static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }
    
    @Override
    public int getStatusCode() {
        return 400;
    }

}
