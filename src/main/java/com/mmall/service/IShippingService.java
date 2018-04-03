package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

import java.util.List;

public interface IShippingService {
    ServerResponse add(Shipping shipping);
    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
    ServerResponse delete(Integer userId, Integer shippingId);
    ServerResponse update(Shipping shipping);
    ServerResponse select(Integer userId, Integer shippingId);
}
