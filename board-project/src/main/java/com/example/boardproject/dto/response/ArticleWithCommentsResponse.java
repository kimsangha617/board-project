package com.example.boardproject.dto.response;

import com.example.boardproject.dto.ArticleWithCommentDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
    Long id,
    String title,
    String content,
    String hashtag,
    LocalDateTime createdAt,
    String email,
    String nickname,
    Set<ArticleCommentResponse> articleCommentsResponse
) implements Serializable {

  public static ArticleWithCommentsResponse from(ArticleWithCommentDto articleDto) {
    String nickname = articleDto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = articleDto.userAccountDto().userId();
    }

    return new ArticleWithCommentsResponse(
        articleDto.id(),
        articleDto.title(),
        articleDto.content(),
        articleDto.hashtag(),
        articleDto.createdAt(),
        articleDto.userAccountDto().email(),
        nickname,
        articleDto.articleCommentDtos().stream()
            .map(ArticleCommentResponse::from)
            .collect(Collectors.toCollection(LinkedHashSet::new))
    );
  }

  public static ArticleWithCommentsResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname, Set<ArticleCommentResponse> articleCommentResponses) {
    return new ArticleWithCommentsResponse(id, title, content, hashtag, createdAt, email, nickname, articleCommentResponses);
  }
}
