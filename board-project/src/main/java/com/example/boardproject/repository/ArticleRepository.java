package com.example.boardproject.repository;

import com.example.boardproject.domain.Article;
import com.example.boardproject.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // Entity안에 있는 모든 필드에 대한 검색기능을 추가해준다
        QuerydslBinderCustomizer<QArticle> //BinderCustomizer의 generic은 QClass 가 들어가게 되어있다
{
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true); // 선택적으로 검색이 가능하게 하고 싶을때,
        // 이 기능에 의해 ModifiedAt, ModifiedBy는 검색 대상에서 제외 하고 싶을때
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 검색이 되게끔 원하는 필드 추가
//        bindings.bind(root.title).first(StringExpression::like); // like '${v}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}