package com.blog.domain.post.service;


import com.blog.domain.post.Post;
import com.blog.domain.post.PostEditor;
import com.blog.domain.post.dto.request.PostCreate;
import com.blog.domain.post.dto.request.PostEdit;
import com.blog.domain.post.dto.request.PostSearch;
import com.blog.domain.post.dto.response.PostResponse;
import com.blog.domain.post.exception.PostNotFound;
import com.blog.domain.post.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public List<PostResponse> findPostAll(PostSearch postSearch) {
        return postRepository.getList(postSearch)
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public PostResponse findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                PostNotFound::new
        );

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Transactional
    public void updatePost(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id).orElseThrow(
                PostNotFound::new
        );

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postBuild = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.update(postBuild);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                PostNotFound::new
        );

        postRepository.delete(post);
    }
}
