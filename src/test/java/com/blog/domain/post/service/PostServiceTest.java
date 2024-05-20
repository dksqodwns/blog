package com.blog.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blog.domain.post.Post;
import com.blog.domain.post.dto.request.PostCreate;
import com.blog.domain.post.dto.request.PostEdit;
import com.blog.domain.post.dto.request.PostSearch;
import com.blog.domain.post.dto.response.PostResponse;
import com.blog.domain.post.exception.PostNotFound;
import com.blog.domain.post.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void write() {
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        postService.write(postCreate);

        assertThat(postRepository.count()).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);

        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void findPostAll() throws Exception {

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목 " + i)
                                .content("내용 " + i)
                                .build()
                ).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        List<PostResponse> response = postService.findPostAll(postSearch);

        assertThat(response.size()).isEqualTo(10);
        assertThat(response.get(0).getTitle()).isEqualTo("제목 30");
        assertThat(response.get(0).getContent()).isEqualTo("내용 30");
    }

    @Test
    @DisplayName("글 1건 조회")
    void findPostById() throws Exception {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostResponse response = postService.findPostById(post.getId());

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("글 1건 조회 실패시")
    void findPostByIdV2() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostNotFound e = assertThrows(PostNotFound.class,
                () -> postService.findPostById(post.getId() + 1L));

        Assertions.assertEquals(
                "존재하지 않는 글 입니다.", e.getMessage()
        );

    }


    @Test
    @DisplayName("글 제목 수정")
    void updatePost() throws Exception {

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit updatePost = PostEdit.builder()
                .title("수정 제목")
                .content("내용")
                .build();

        postService.updatePost(post.getId(), updatePost);
        Post findUpdatePost = postRepository.findById(post.getId()).orElseThrow(
                () -> new RuntimeException("글이 존재하지 않습니다.")
        );

        assertThat(findUpdatePost.getTitle()).isEqualTo("수정 제목");
        assertThat(findUpdatePost.getContent()).isEqualTo("내용");
    }


    @Test
    @DisplayName("글 내용 수정")
    void updatePostV2() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit updatePost = PostEdit.builder()
                .title("제목")
                .content("수정 내용")
                .build();

        postService.updatePost(post.getId(), updatePost);
        Post findUpdatePost = postRepository.findById(post.getId()).orElseThrow(
                () -> new RuntimeException("글이 존재하지 않습니다.")
        );

        assertThat(findUpdatePost.getTitle()).isEqualTo("제목");
        assertThat(findUpdatePost.getContent()).isEqualTo("수정 내용");
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        postService.deletePost(post.getId());
        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 삭제 존재하지 않는 글")
    void deletePostV2() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostNotFound e = assertThrows(PostNotFound.class,
                () -> postService.deletePost(post.getId() + 1L));

        Assertions.assertEquals(
                "존재하지 않는 글 입니다.", e.getMessage()
        );
    }
}