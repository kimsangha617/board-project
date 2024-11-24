package com.example.boardproject.dto.response;

import com.example.boardproject.dto.ArticleCommentDto;
import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleCommentResponse(
    Long id,
    String content,
    LocalDateTime createdAt,
    String email,
    String nickname
) implements Serializable {

  public static ArticleCommentResponse from(ArticleCommentDto articleCommentDto) {
    String nickname = articleCommentDto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = articleCommentDto.userAccountDto().userId();
    }

    return new ArticleCommentResponse(
        articleCommentDto.id(),
        articleCommentDto.content(),
        articleCommentDto.createdAt(),
        articleCommentDto.userAccountDto().email(),
        nickname
    );
  }
}
