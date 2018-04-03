package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    ServerResponse addCategory(String categoryName, int parentId);
    ServerResponse setCategoryName(int categoryId, String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId);
    ServerResponse<List<Integer>>  selectCategoryAndChildrenById(int categoryId);
}
