package com.example.articleManagement.service.impl;

import com.example.articleManagement.entity.Category;
import com.example.articleManagement.repository.CategoryRepository;
import com.example.articleManagement.service.CategoryService;
import com.example.articleManagement.util.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    //添加分类
    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        String id = (String) map.get("id");
        category.setCreateUser(id);

        //see article add
        category.setId(null);

        repository.save(category);
    }

    //显示所有分类
    @Override
    public List<Category> list() {
        Map<String,Object> map = ThreadLocalUtil.get();
        String id = (String) map.get("id");
        return repository.findByCreateUser(id);
    }

    //查询某人的分类设置
    @Override
    public Category findById(String id) {return repository.findCategoryById(id);
    }

    //更新分类
    @Override
    public void update(Category category) {
        Category existingCategory = findById(category.getId());
        if(category.getCategoryAlias() != null) {
            existingCategory.setCategoryAlias(category.getCategoryAlias());
        }
        if(category.getCategoryName()!=null) {
            existingCategory.setCategoryName(category.getCategoryName());
        }
        existingCategory.setUpdateTime(LocalDateTime.now());

        repository.save(existingCategory);
    }

    //删除分类
    public void delete(String id) {
        repository.deleteById(id);
    }
}
