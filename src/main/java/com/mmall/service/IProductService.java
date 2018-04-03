package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

public interface IProductService {

    ServerResponse<String> saveOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId,
                                           Integer pageNum, Integer pageSize);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId,
                                                         Integer pageNum, Integer pageSize,
                                                         String orderBy);
}
