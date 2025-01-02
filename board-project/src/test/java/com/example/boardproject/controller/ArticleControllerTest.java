package com.example.boardproject.controller;

import com.example.boardproject.config.SecurityConfig;
import com.example.boardproject.domain.type.SearchType;
import com.example.boardproject.dto.ArticleCommentDto;
import com.example.boardproject.dto.ArticleWithCommentDto;
import com.example.boardproject.dto.UserAccountDto;
import com.example.boardproject.service.ArticleService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.boardproject.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    //Mockito 의 mock 과 같음... spring slice test 에서 주로 사용
    //Controller 단에 ArticleService 와의 의존성을 끊기 위해 사용
    @MockBean
    private ArticleService articleService;

    @MockBean
    private PaginationService paginationService;

    private final MockMvc mvc;

    // 생성자 주입에 @MockBean 어노테이션이 추가된 ArticleService 는 추가하지 않는다. @MockBean은 ElementType에 Method가 없다. 즉 메서드에 파라미터로 넣을수 없다는 얘기
    // @WebMvcTest안에 있는 @ExtendWith의 SpringExtension 에서 @Autowired 어노테이션을 인지해서 생성자 주입을 할 수 있게 하는 코드가 있기 때문
    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //given

        // any()는 Matcher 메소드인데 메소드의 필드로 어떤것만 matcher를 쓸 수 없다 따라서 eq() 메소드를 사용해야한다
        // 따라서 eq() 메소드를 사용하여 Null 값을 넣어준다
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class)))
            .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));


        //when&then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
        ;

        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        //given

        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";

        // any()는 Matcher 메소드인데 메소드의 필드로 어떤것만 matcher를 쓸 수 없다 따라서 eq() 메소드를 사용해야한다
        // 따라서 eq() 메소드를 사용하여 Null 값을 넣어준다
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class)))
                .willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));


        //when&then
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));

        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        //when&then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
        ;
        then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        //given

        //when&then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search"))
        ;
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        //given

        //when&then
        mvc.perform(get("/articles/sarch-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search-hashtag"))
        ;
    }


    private ArticleWithCommentDto createArticleWithCommentsDto() {
        return ArticleWithCommentDto.of(
            1L,
            createUserAccountDto(),
            Set.of(),
            "title",
            "content",
            "#java",
            LocalDateTime.now(),
            "kim",
            LocalDateTime.now(),
            "kim"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
            "kim",
            "pw",
            "kim@mail.com",
            "kim",
            "memokim",
            LocalDateTime.now(),
            "kim",
            LocalDateTime.now(),
            "kim"
        );
    }
}
