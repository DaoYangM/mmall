package com.mmall.conotroller.portal;

import com.mmall.aop.UserLoginAOP;
import com.mmall.common.NeedLogin;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/shipping/")
@ResponseBody
public class ShippingController {

    @Autowired
    private UserLoginAOP loginAOP;

    @Autowired
    private IShippingService shippingService;

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @NeedLogin
    public ServerResponse add(HttpSession session, Shipping shipping) {
        User user = loginAOP.getUser();
        if (user != null) {
            shipping.setUserId(user.getId());
            return shippingService.add(shipping);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = loginAOP.getUser();
        if (user != null) {
            return shippingService.list(user.getId(), pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    @NeedLogin
    public ServerResponse delete(HttpSession session,
                               @RequestParam(value = "shippingId") Integer shippingId) {
        if (shippingId == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        User user = loginAOP.getUser();
        if (user != null) {
            return shippingService.delete(user.getId(), shippingId);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @NeedLogin
    public ServerResponse update(HttpSession session, Shipping shipping) {
        User user = loginAOP.getUser();
        if (user != null) {
            shipping.setUserId(user.getId());
            return shippingService.update(shipping);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse select(HttpSession session, Integer shippingId) {
        if (shippingId == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        User user = loginAOP.getUser();
        if (user != null) {
            return shippingService.select(user.getId(), shippingId);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }
}
