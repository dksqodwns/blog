package com.blog.domain.post.repository;

import com.blog.domain.post.Post;
import com.blog.domain.post.dto.request.PostSearch;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);


}
