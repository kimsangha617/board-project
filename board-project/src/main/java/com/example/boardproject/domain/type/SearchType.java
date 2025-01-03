package com.example.boardproject.domain.type;

import lombok.Getter;

@Getter
public enum SearchType {
    TITLE("제목"),
    CONTENT("본문"),
    ID("아이디"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
