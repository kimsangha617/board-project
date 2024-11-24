package com.example.boardproject.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Getter
@ToString(callSuper = true) // AuditingFields 의 생성일시, 생성자 등 모두 toString을 찍기 위해 설정
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount;

    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @Setter
    private String hashtag;

//    @ToString.Exclude
//    @JoinTable(
//            name = "article_hashtag",
//            joinColumns = @JoinColumn(name = "articleId"),
//            inverseJoinColumns = @JoinColumn(name = "hashtagId")
//    )
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private Set<ArticleComment> hashtags = new LinkedHashSet<>(); // 해시태그

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    // 댓글 리스트를 굳이 다 뽑아보지 않아도 되니까 ? 여기서 exclude,, 쓰지 않으면 순환참조 발생 가능, Article 의 커멘트를 toString 커멘트에서 Article 을 toString
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    protected Article() {
    }

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Article article)) return false;
        return id != null && Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
