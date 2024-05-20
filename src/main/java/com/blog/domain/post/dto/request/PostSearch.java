package com.blog.domain.post.dto.request;

import static java.lang.Math.max;
import static java.lang.Math.min;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 20;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

}
