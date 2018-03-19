package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
