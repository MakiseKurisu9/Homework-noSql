package com.example.articleManagement.repository;

import com.example.articleManagement.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<Article,String> {
    Article findArticleById(String id);

}
