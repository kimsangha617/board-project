package com.example.boardproject.service;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.type.SearchType;
import com.example.boardproject.dto.ArticleDto;
import com.example.boardproject.dto.ArticleUpdateDto;
import com.example.boardproject.dto.ArticleWithCommentDto;
import com.example.boardproject.repository.ArticleRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) { // isBlank 빈 문자나 스페이스 경우 확인
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        //TODO #을 검색어로 넣는경우 ##text 가 들어가기 때문에 검색이 안될텐데 추후 리팩토링
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }


    @Transactional(readOnly = true)
    public ArticleWithCommentDto getArticle(long articleId) {
        return articleRepository.findById(articleId)
            .map(ArticleWithCommentDto::from)
            .orElseThrow( () -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId))
            ;
    }

    public void saveArticle(ArticleDto articleDto) {
        articleRepository.save(articleDto.toEntity());
    }

    @Transactional
    public void updateArticle(ArticleDto articleDto) {
        // article 이 존재 하는것을 알고 가져와서 업데이트 하고싶음
        // 없다면 exception 처리하면 된다
        // 하지만 findById 하게 되면 select 쿼리가 발생하므로 불필요한 트랜잭션이 생기게 된다
        // 영속성 컨텍스트로 엔티티 정보만 가져오고 싶다.. 그럴때 쓰는게 getReferenceById
        try {

        Article article = articleRepository.getReferenceById(articleDto.id());

        if (articleDto.title() != null) {
            article.setTitle(articleDto.title());
        }

        if (articleDto.content() != null) {
            article.setContent(articleDto.content());
        }
            article.setHashtag(articleDto.hashtag());
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패, 게시글을 찾을수 없습니다. - dto: {}", articleDto);
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
