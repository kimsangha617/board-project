package com.example.boardproject.repository;

import com.example.boardproject.config.JpaConfig;
import com.example.boardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        //given

        //when
        List<Article> articleList = articleRepository.findAll();
        //then
        assertThat(articleList)
                .isNotNull()
                .hasSize(100)
                ;
    }

//    @DisplayName("select test")
//    @Test
//    void givenTestData_whenInserting_thenWorksFine() {
//        //given
//        long previousCount = articleRepository.count();
//        Article article = Article.of("new Article", "new Content", "#spring");
//        //when
//        Article savedArticle = articleRepository.save(article);
//
//        //then
//        assertThat(articleRepository.count())
//                .isEqualTo(previousCount+1)
//        ;
//    }

//    @DisplayName("select test")
//    @Test
//    void givenTestData_whenUpdating_thenWorksFine() {
//        //given
//        Article article = articleRepository.findById(1L).orElseThrow();
//        String updatedHashtag = "#springboot";
//        article.setHashtag(updatedHashtag);
//
//        //when
//        Article savedArticle = articleRepository.saveAndFlush(article);
//
//        //then
//        assertThat(savedArticle)
//                .hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
//        ;
//
//    }
}