package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface ICategoryService {
    ServerResponse addCategory(String categoryName, int parentId);
    ServerResponse setCategoryName(int categoryId, String categoryName);
}
