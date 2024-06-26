package com.blog.domain.post.exception;

public class PostNotFound extends BlogException {

    public static final String MESSAGE = "존재하지 않는 글 입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
