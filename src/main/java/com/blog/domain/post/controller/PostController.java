package com.blog.domain.post.controller;

import com.blog.domain.post.dto.request.PostCreate;
import com.blog.domain.post.dto.request.PostEdit;
import com.blog.domain.post.dto.request.PostSearch;
import com.blog.domain.post.dto.response.PostResponse;
import com.blog.domain.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
    }

    @GetMapping("/posts")
    public List<PostResponse> findPostAll(@ModelAttribute PostSearch postSearch) {
        return postService.findPostAll(postSearch);
    }

    @GetMapping("/posts/{id}")
    public PostResponse findPostById(@PathVariable Long id) throws Exception {
        return postService.findPostById(id);
    }

    @PatchMapping("/posts/{id}")
    public void updatePost(@PathVariable Long id, @RequestBody @Valid PostEdit request) {
        postService.updatePost(id, request);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
