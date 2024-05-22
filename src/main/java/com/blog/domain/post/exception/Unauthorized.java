package com.blog.domain.post.exception;

public class Unauthorized extends BlogException {
    public static final String MESSAGE = "로그인이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
