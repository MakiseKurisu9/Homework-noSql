package com.example.articleManagement.service;

import com.example.articleManagement.entity.Article;
import com.example.articleManagement.entity.PageBean;

public interface ArticleService {
    void add(Article article);

    Article getArticle(String id);

    void updateArticle(Article article);

    void delete(String id);

    PageBean<Article> list(Integer pageNum, Integer pageSize, String categoryId, String state);
}
