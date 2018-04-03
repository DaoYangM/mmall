package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Shipping shipping) {
        int result = shippingMapper.insert(shipping);
        if (result == 1) {
            return ServerResponse.createBySuccess("Delivery address added successfully!");
        }
        return ServerResponse.createByErrorMessage("DFailed to add the shipping address!");
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        List<Shipping> shippingList = shippingMapper.selectShippingByUserId(userId);
        PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = new PageInfo(shippingList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
        int result = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        if (result == 1) {
            return ServerResponse.createBySuccess("Delivery address deleted successfully!");
        }
        return ServerResponse.createByErrorMessage("DFailed to delete the shipping address!");
    }

    @Override
    public ServerResponse update(Shipping shipping) {
        int result = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (result == 1) {
            return ServerResponse.createBySuccess("Delivery address updated successfully!");
        }
        return ServerResponse.createByErrorMessage("Failed to updated the shipping address!");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);

        return ServerResponse.createBySuccess(shipping);
    }
}
