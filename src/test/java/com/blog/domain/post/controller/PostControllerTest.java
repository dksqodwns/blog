package com.blog.domain.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.post.Post;
import com.blog.domain.post.dto.request.PostCreate;
import com.blog.domain.post.dto.request.PostEdit;
import com.blog.domain.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("post 요청시 200 응답코드를 보낸다.")
    void test() throws Exception {
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("post 요청시 값이 부족하면 400 에러를 보낸다.")
    void test2() throws Exception {
        PostCreate request = PostCreate.builder()
                .content("내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 입니다."))
                .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
                .andDo(print());

    }

    @Test
    @DisplayName("post 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(postRepository.count()).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);

        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목 " + i)
                                .content("내용 " + i)
                                .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        mockMvc.perform(get("/posts?page=1&size=10&sort=id,desc")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목 30"))
                .andExpect(jsonPath("$[0].content").value("내용 30"))
                .andDo(print());
    }


    @Test
    @DisplayName("페이지가 0으로 넘어오면 첫 페이지 조회")
    void test6() throws Exception {
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목 " + i)
                                .content("내용 " + i)
                                .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        mockMvc.perform(get("/posts?page=0&size=10&sort=id,desc")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목 30"))
                .andExpect(jsonPath("$[0].content").value("내용 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1건 조회")
    void test4() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        mockMvc.perform(get("/posts/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andDo(print());

        assertThat(postRepository.count()).isEqualTo(1L);

        Post findPost = postRepository.findAll().get(0);

        assertThat(findPost.getTitle()).isEqualTo("제목");
        assertThat(findPost.getContent()).isEqualTo("내용");
    }


    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("수정 제목")
                .content("내용")
                .build();
        mockMvc.perform(patch("/posts/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test8() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        mockMvc.perform(delete("/posts/{id}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}