package com.example.articleManagement.controller;

import com.example.articleManagement.entity.Article;
import com.example.articleManagement.entity.PageBean;
import com.example.articleManagement.entity.Result;
import com.example.articleManagement.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService service;

    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        service.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String state
    ) {
        PageBean<Article> pageBean = service.list(pageNum,pageSize,categoryId,state);
        return Result.success(pageBean);
    }

    @GetMapping("/detail")
    public Result<Article> getArticle(String id) {
        Article article = service.getArticle(id);
        return Result.success(article);
    }

    @PutMapping
    public Result<Article> updateArticle(@RequestBody Article article) {
        service.updateArticle(article);
        Article updateArticle = service.getArticle(article.getId());
        return Result.success(updateArticle);
    }


    @DeleteMapping
    public Result delete(String id) {
        service.delete(id);
        return Result.success();
    }

}
