package com.example.articleManagement.service.impl;

import com.example.articleManagement.entity.Article;
import com.example.articleManagement.entity.PageBean;

import com.example.articleManagement.repository.ArticleRepository;
import com.example.articleManagement.service.ArticleService;
import com.example.articleManagement.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;


    //添加文章
    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Map<String,Object> map = ThreadLocalUtil.get();
        String userId = (String)map.get("id");
        article.setCreateUser(userId);
        //和mysql不一样 没有自增id 所以设置为null 让其自动随机生成
        //后发现 不设置为null，mongodb会自动设置，不需要更改
        article.setId(null);

        repository.save(article);
    }

    //获取自己写的文章
    @Override
    public Article getArticle(String id) {
        return repository.findArticleById(id);
    }

    //更新写的文章
    @Override
    public void updateArticle(Article article) {
        // 从数据库中获取当前的记录
        //put请求只会提交更改后的属性，mongodb如果使用save方法会直接覆盖，所以手动更新
        Article existingArticle = repository.findById(article.getId())
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // 更新需要变更的字段
        if (article.getTitle() != null) {
            existingArticle.setTitle(article.getTitle());
        }
        if (article.getContent() != null) {
            existingArticle.setContent(article.getContent());
        }
        if (article.getCategoryId() != null) {
            existingArticle.setCategoryId(article.getCategoryId());
        }
        if (article.getState() != null) {
            existingArticle.setState(article.getState());
        }
        if(article.getCoverImg() != null) {
            existingArticle.setCoverImg(article.getCoverImg());
        }
        if(article.getState() != null) {
            existingArticle.setState(article.getState());
        }

        // 更新更新时间
        existingArticle.setUpdateTime(LocalDateTime.now());

        // 获取当前用户信息（仅用于记录更新者）
        Map<String, Object> map = ThreadLocalUtil.get();
        String userId = (String) map.get("id");
        existingArticle.setCreateUser(userId);
        // 保存更新后的记录
        repository.save(existingArticle);
    }


    //删除文章
    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    //pageNum页码,pageSize每页条数
    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, String categoryId, String state) {
        PageBean<Article> pageBean = new PageBean<>();
        Query query = new Query();

        Map<String,Object> map = ThreadLocalUtil.get();
        String userId = (String) map.get("id");

        //dynamic sql
        if(userId!=null) {
            query.addCriteria(Criteria.where("createUser").is(userId));
            if(categoryId != null) {
                query.addCriteria(Criteria.where("categoryId").is(categoryId));
            }
            if(state != null) {
                query.addCriteria(Criteria.where("state").is(state));
            }
        }

        // 计算分页
        //Spring Data 用来进行分页查询的对象，它使用的是 从 0 开始的页码。当你请求第 1 页的数据时，实际上在 PageRequest.of() 中应该传入 0 作为 pageNum
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        // 将分页条件添加到查询中
        query.with(pageable);


        List<Article> list = mongoTemplate.find(query,Article.class);

        long total = mongoTemplate.count(query.skip(-1).limit(-1),Article.class);

        pageBean.setItems(list);
        pageBean.setTotal(total);

        return pageBean;
    }
}
