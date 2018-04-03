package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Cart;
import com.mmall.vo.CartVo;

public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count, Integer checked);
    ServerResponse<CartVo> delete(Integer userId, String productId);
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> selectAllOrUnselectAll(Integer userId, Integer checked);
    ServerResponse<CartVo> selectOne(Integer userId, Integer productId, Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
