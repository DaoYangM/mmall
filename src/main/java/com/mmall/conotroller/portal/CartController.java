package com.mmall.conotroller.portal;

import com.mmall.aop.UserLoginAOP;
import com.mmall.common.Const;
import com.mmall.common.NeedLogin;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequestMapping(value = "/cart/")
@ResponseBody
@Controller
public class CartController {

    @Autowired
    private UserLoginAOP userLoginAOP;

    @Autowired
    private ICartService cartService;

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse add(HttpSession session) {
        User user = userLoginAOP.getUser();
        if (user != null) {
            return cartService.list(user.getId());
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @NeedLogin
    public ServerResponse add(HttpSession session, Integer productId, Integer count) {
        User user = userLoginAOP.getUser();
        if (productId == null || count == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        if (user != null) {
            return cartService.add(user.getId(), productId, count);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @NeedLogin
    public ServerResponse update(HttpSession session, Integer productId, Integer count,
                                 @RequestParam(value ="checked", defaultValue = "1") Integer checked) {
        User user = userLoginAOP.getUser();
        if (productId == null || count == null || count < 0)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        if (user != null) {
            return cartService.update(user.getId(), productId, count, checked);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    @NeedLogin
    public ServerResponse update(HttpSession session, String productId) {
        User user = userLoginAOP.getUser();
        if (productId == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        if (user != null) {
            return cartService.delete(user.getId(), productId);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "select_all.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse select_all(HttpSession session) {
        User user = userLoginAOP.getUser();
        if (user != null) {
            return cartService.selectAllOrUnselectAll(user.getId(), Const.Cart.CHECKED);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "un_select_all.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse un_select_all(HttpSession session) {
        User user = userLoginAOP.getUser();
        if (user != null) {
            return cartService.selectAllOrUnselectAll(user.getId(), Const.Cart.UNCHECKED);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "select_one.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse select_one(HttpSession session, Integer productId) {
        User user = userLoginAOP.getUser();
        if (productId == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        if (user != null) {
            return cartService.selectOne(user.getId(), productId, Const.Cart.CHECKED);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "un_select_one.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse un_select_one(HttpSession session, Integer productId) {
        User user = userLoginAOP.getUser();
        if (productId == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        if (user != null) {
            return cartService.selectOne(user.getId(), productId, Const.Cart.UNCHECKED);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }

    @RequestMapping(value = "get_cart_product_count.do", method = RequestMethod.GET)
    @NeedLogin
    public ServerResponse get_cart_product_count(HttpSession session) {
        User user = userLoginAOP.getUser();

        if (user != null) {
            return cartService.getCartProductCount(user.getId());
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
    }
}
