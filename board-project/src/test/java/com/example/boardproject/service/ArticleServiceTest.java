package com.example.boardproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.Hashtag;
import com.example.boardproject.domain.UserAccount;
import com.example.boardproject.domain.type.SearchType;
import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.UserAccountDto;
import com.example.boardproject.repository.ArticleRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

  @InjectMocks // mock을 주입하는 대상에게
  private ArticleService sut; //system under test - 이녀석이 테스트 대상이다

  @Mock // 그 외 나머지 mock 에 붙여줌
  private ArticleRepository articleRepository;

  @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
  @Test
  void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
    //given
    Pageable pageable = Pageable.ofSize(20);
    given(articleRepository.findAll(pageable)).willReturn(Page.empty());

    //when
    Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

    //then
    assertThat(articles).isEmpty();
    then(articleRepository).should().findAll(pageable);

  }

  @DisplayName("게시글을 검색하면 게시글 리스트를 반환한다")
  @Test
  void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
    //given
//        SearchParameters param = SearchParameters.of(SearchType.TITLE, "search keyword");

    SearchType searchType = SearchType.TITLE;
    String searchKeyword = "title";
    Pageable pageable = Pageable.ofSize(20);
    given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(
        Page.empty());

    //when
    Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword,
        pageable); // 제목, 본문, id, nickname, hashtag

    //then
    assertThat(articles).isEmpty();
    then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);

  }

//    @DisplayName("게시글을 조회하면, 게시글을 반환한다")
//    @Test
//    void givenArticleId_whenSearchingArticles_thenReturnsArticle() {
//        //given
//        Long articleId = 1L;
//        Article article = createArticleDto();
//        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
//        //when
//        ArticleWithCommentDto dto = sut.getArticle(articleId);
//
//        //then
//        assertThat(dto)
//            .hasFieldOrPropertyWithValue("title", article.getTitle())
//            .hasFieldOrPropertyWithValue("content", article.getContent())
//            .hasFieldOrPropertyWithValue("hashtagDtos", article.getHashtags().stream()
//                .map(HashtagDto::from)
//                .collect(Collectors.toUnmodifiableList()));
//        then(articleRepository).should().findById(articleId);
//    }

  @DisplayName("게시글이 없으면, 예외를 던진다")
  @Test
  void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
    //given
    Long articleId = 1L;
    given(articleRepository.findById(articleId)).willReturn(Optional.empty());

    //when
    Throwable t = catchThrowable(() -> sut.getArticle(articleId));

    //then
    assertThat(t)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("게시글이 없습니다 - articleId: " + articleId);
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("게시글 정보를 입력하면 게시글을 생성한다")
  @Test
  void givenArticleInfo_whenSavingArticle_thenSaveArticle() {
    //given
    ArticleDto articleDto = createArticleDto();
    given(articleRepository.save(any(Article.class))).willReturn(
        createArticle()); // articleRepository 의존성에 save가 호출될거다 라는걸 코드로 보여주는 의미, 딱히 크게 의미 없음

    //when
    sut.saveArticle(articleDto);

    //then
    then(articleRepository).should()
        .save(any(Article.class)); // articleRepository에서 save가 한 번 호출이 되었는가 검사하는 코드
  }

  @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다")
  @Test
  void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    //given
    Article article = createArticle();
    ArticleDto dto = createArticleDto("new title", "new content", "new hashtag");
    given(articleRepository.getReferenceById(dto.id())).willReturn(article);

    //when
    sut.updateArticle(dto);

    //then
    assertThat(article)
        .hasFieldOrPropertyWithValue("title", dto.title())
        .hasFieldOrPropertyWithValue("content", dto.content())
        .hasFieldOrPropertyWithValue("hashtag", dto.hashtag())
    ;
    then(articleRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
  @Test
  void givenNonexistentArticleInfo_whenUpdatingArticle_thenLongWarningAndDoNothing() {
    //given
    ArticleDto dto = createArticleDto("new title", "new content", "new hashtag");

    given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

    //when
    sut.updateArticle(dto);

    //then
    then(articleRepository).should().getReferenceById(dto.id());
  }


  @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    //given
    Long articleId = 1L;
    willDoNothing().given(articleRepository).deleteById(articleId);

    //when
    sut.deleteArticle(1L);

    //then
    then(articleRepository).should().deleteById(articleId);
  }

  private UserAccount createUserAccount() {
    return createUserAccount("sanhocho");
  }

  private UserAccount createUserAccount(String userId) {
    return UserAccount.of(
        userId,
        "password",
        "sanghocho@naver.com",
        "Sanhocho",
        null
    );
  }

  private Article createArticle() {
    return Article.of(
        createUserAccount(),
        "title",
        "content",
        "#java"
    );
  }

  private ArticleDto createArticleDto() {
    return createArticleDto("title", "content", "#java");
  }

  private ArticleDto createArticleDto(String title, String content, String hashtag) {
    return ArticleDto.of(
        1L,
        createUserAccountDto(),
        title,
        content,
        hashtag,
        LocalDateTime.now(),
        "KIM",
        LocalDateTime.now(),
        "Kim"
    );
  }

  private Hashtag createHashtag(String hashtagName) {
    return createHashtag(1L, hashtagName);
  }

  private Hashtag createHashtag(Long id, String hashtagName) {
    Hashtag hashtag = Hashtag.of(hashtagName);
    ReflectionTestUtils.setField(hashtag, "id", id);
    return hashtag;
  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
        "sanhocho",
        "password",
        "sanghocho@naver.com",
        "Sanhocho",
        "This is Blue San ho cho",
        LocalDateTime.now(),
        "ssanhocho",
        LocalDateTime.now(),
        "ssanhocho"
    );
  }

}