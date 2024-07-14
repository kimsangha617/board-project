package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.type.SearchType;
import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.ArticleUpdateDto;
import com.example.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks // mock을 주입하는 대상에게
    private ArticleService sut; //system under test - 이녀석이 테스트 대상이다

    @Mock // 그 외 나머지 mock 에 붙여줌
    private ArticleRepository articleRepository;

    @DisplayName("게시글을 검색하면 게시글 리스트를 반환한다")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList() {
        //given
//        SearchParameters param = SearchParameters.of(SearchType.TITLE, "search keyword");
        //when
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, id, nickname, hashtag

        //then
        assertThat(articles).isNotNull();

    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다")
    @Test
    void givenArticleId_whenSearchingArticles_thenReturnsArticle() {
        //given
//        SearchParameters param = SearchParameters.of(SearchType.TITLE, "search keyword");
        //when
        ArticleDto article = sut.searchArticle(1L);

        //then
        assertThat(article).isNotNull();
    }

//    @DisplayName("게시글 정보를 입력하면 게시글을 생성한다")
//    @Test
//    void givenArticleInfo_whenSavingArticle_thenSaveArticle() {
//        //given
//        ArticleDto dto = ArticleDto.of(LocalDateTime.now(), "tester", "title", "content", "hashtag");
//        given(articleRepository.save(any(Article.class))).willReturn(null); // articleRepository 의존성에 save가 호출될거다 라는걸 코드로 보여주는 의미, 딱히 크게 의미 없음
//
//        //when
//        sut.saveArticle(dto);
//
//        //then
//        then(articleRepository).should().save(any(Article.class)); // articleRepository에서 save가 한 번 호출이 되었는가 검사하는 코드
//    }

    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
        //given
        ArticleUpdateDto dto = ArticleUpdateDto.of("title modified", "content", "hashtag");
        given(articleRepository.save(any(Article.class))).willReturn(null); // articleRepository 의존성에 save가 호출될거다 라는걸 코드로 보여주는 의미, 딱히 크게 의미 없음

        //when
        sut.updateArticle(1L, dto);

        //then
        then(articleRepository).should().save(any(Article.class)); // articleRepository에서 save가 한 번 호출이 되었는가 검사하는 코드
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        //given
        ArticleUpdateDto dto = ArticleUpdateDto.of("title modified", "content", "hashtag");
        willDoNothing().given(articleRepository).delete(any(Article.class)); // articleRepository 의존성에 save가 호출될거다 라는걸 코드로 보여주는 의미, 딱히 크게 의미 없음

        //when
        sut.deleteArticle(1L);

        //then
        then(articleRepository).should().delete(any(Article.class)); // articleRepository에서 save가 한 번 호출이 되었는가 검사하는 코드
    }

}