package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, int parentId) {
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);

        int result = categoryMapper.insert(category);

        if (result == 1) {
            return ServerResponse.createBySuccessMessage("Insert success");
        }

        return ServerResponse.createByErrorMessage("Insert failure");
    }

    @Override
    public ServerResponse setCategoryName(int categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int result = categoryMapper.updateByPrimaryKeySelective(category);

        if (result == 1) {
            return ServerResponse.createBySuccessMessage("Insert success");
        }
        return ServerResponse.createByErrorMessage("Insert failure");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId) {
        List<Category> categoryList = categoryMapper.getChildrenParallelCategory(categoryId);
        return ServerResponse.createBySuccess("getChildrenParallelCategory success", categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(int categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        List<Integer> integerList = Lists.newArrayList();
        categorySet = findChildCategory(categorySet, categoryId);

        for (Category category: categorySet) {
            integerList.add(category.getId());
        }

        return ServerResponse.createBySuccess(integerList);
    }

    public Set<Category> findChildCategory(Set<Category> categorySet, int categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null)
            categorySet.add(category);
        try {
            List<Category> categoryList = categoryMapper.getChildrenParallelCategory(category.getId());
            for (Category category1: categoryList) {
                findChildCategory(categorySet, category1.getId());
            }
        } catch (NullPointerException e) {
            return categorySet;
        }
        return categorySet;
    }
}
