package com.mmall.conotroller.backend;

import com.mmall.aop.UserLoginAOP;
import com.mmall.common.NeedLogin;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequestMapping(value = "/manage/category/")
@Controller
public class CategoryManageController {

    @Autowired
    private UserLoginAOP userLoginAOP;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    @NeedLogin
    public ServerResponse addCategory(HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        if (StringUtils.isNotBlank(categoryName) && parentId >= 0 ){
            User user = userLoginAOP.getUser();
            if (user != null) {
                if (userService.checkAdmin(user))
                    return categoryService.addCategory(categoryName, parentId);
                else
                    return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_ADMIN.getCode()
                            , "Permission deny");
            } else
                return ServerResponse.createByErrorMessage("Need login");
        }
            return ServerResponse.createByErrorMessage("RequestParam Error");
    }

    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    @NeedLogin
    public ServerResponse setCategoryName(HttpSession session, int categoryId, String categoryName) {
        if (StringUtils.isNotBlank(categoryName) && categoryId >= 0 ){
            User user = userLoginAOP.getUser();
            if (user != null) {
                if (userService.checkAdmin(user))
                    return categoryService.setCategoryName(categoryId, categoryName);
                else
                    return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_ADMIN.getCode()
                            , "Permission deny");
            } else
                return ServerResponse.createByErrorMessage("Need login");
        }
        return ServerResponse.createByErrorMessage("RequestParam Error");
    }

    @RequestMapping(value = "get_category.do")
    @ResponseBody
    @NeedLogin
    public ServerResponse getCategory(HttpSession session, int categoryId) {
        if (categoryId >= 0 ){
            User user = userLoginAOP.getUser();
            if (user != null) {
                return categoryService.getChildrenParallelCategory(categoryId);
            } else
                return ServerResponse.createByErrorMessage("Need login");
        }
        return ServerResponse.createByErrorMessage("RequestParam Error");
    }

    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    @NeedLogin
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, int categoryId) {
        if (categoryId >= 0 ){
            User user = userLoginAOP.getUser();
            if (user != null) {
                return categoryService.selectCategoryAndChildrenById(categoryId);
            } else
                return ServerResponse.createByErrorMessage("Need login");
        }
        return ServerResponse.createByErrorMessage("RequestParam Error");
    }
}
