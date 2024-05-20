package com.blog.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostEdit {

    @NotBlank(message = "타이틀을 입력하세요.")
    private final String title;

    @NotBlank(message = "내용을 입력하세요.")
    private final String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
