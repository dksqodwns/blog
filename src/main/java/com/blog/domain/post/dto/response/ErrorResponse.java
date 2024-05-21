package com.blog.domain.post.dto.response;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
//@JsonInclude(Include.NON_EMPTY) json값이 비어있는건 제외하고 내보내는 설정
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
