package com.example.boardproject.dto.response;

import com.example.boardproject.dto.ArticleDto;
import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleResponse (
    Long id,
    String title,
    String content,
    String hashtag,
    LocalDateTime createdAt,
    String email,
    String nickname
) implements Serializable {

  public static ArticleResponse from(ArticleDto articleDto) {
    String nickname = articleDto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = articleDto.userAccountDto().userId();
    }

    return new ArticleResponse(
        articleDto.id(),
        articleDto.title(),
        articleDto.content(),
        articleDto.hashtag(),
        articleDto.createdAt(),
        articleDto.userAccountDto().email(),
        nickname
    );
  }
}
