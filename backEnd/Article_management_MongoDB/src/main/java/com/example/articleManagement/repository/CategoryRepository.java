package com.example.articleManagement.repository;

import com.example.articleManagement.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends MongoRepository<Category,String> {

    // 根据创建用户查询分类列表
    List<Category> findByCreateUser(String createUser);

    Category findCategoryById(String id);
}
