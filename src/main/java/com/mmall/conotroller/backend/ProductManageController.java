package com.mmall.conotroller.backend;
import com.google.common.collect.Maps;
import com.mmall.aop.UserLoginAOP;
import com.mmall.common.NeedLogin;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/product/")
public class ProductManageController {

    @Autowired
    private UserLoginAOP userLoginAOP;

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IFileService fileService;

    @RequestMapping(value = "product_save.do", method = RequestMethod.POST)
    @ResponseBody
    @NeedLogin
    public ServerResponse productSave(HttpSession session, Product product) {
        if (product != null){
            User user = userLoginAOP.getUser();
            if (user != null) {
                if (userService.checkAdmin(user))
                    return productService.saveOrUpdateProduct(product);
                else
                    return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_ADMIN.getCode()
                            , "Permission deny");
            } else
                return ServerResponse.createByErrorMessage("Need login");
        }
        return ServerResponse.createByErrorMessage("RequestParam Error");
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    @NeedLogin
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        if (productId != null){
            User user = userLoginAOP.getUser();
            if (user != null) {
                if (userService.checkAdmin(user))
                    return productService.setSaleStatus(productId, status);
                else
                    return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_ADMIN.getCode()
                            , "Permission deny");
            } else
                return ServerResponse.createByErrorMessage("Need login");
        }
        return ServerResponse.createByErrorMessage("RequestParam Error");
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    @NeedLogin
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        if (productId != null){
            User user = userLoginAOP.getUser();
            if (user != null) {
                if (userService.checkAdmin(user))
                    return productService.manageProductDetail(productId);
                else
                    return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_ADMIN.getCode()
                            , "Permission deny");
            } else
                return ServerResponse.createByErrorMessage("Need login");
        }
        return ServerResponse.createByErrorMessage("RequestParam Error");
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    @NeedLogin
    public ServerResponse getDetail(HttpSession session,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        User user = userLoginAOP.getUser();
        if (user != null) {
            if (userService.checkAdmin(user))
                return productService.getProductList(pageNum, pageSize);
            else
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_ADMIN.getCode()
                        , "Permission deny");
        } else
            return ServerResponse.createByErrorMessage("Need login");
    }

    @RequestMapping(value = "search.do", method = RequestMethod.GET)
    @ResponseBody
    @NeedLogin
    public ServerResponse getDetail(HttpSession session,
                                    String productName,
                                    Integer productId,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        if (!StringUtils.isNotBlank(productName))
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        User user = userLoginAOP.getUser();
        if (user != null) {
            if (userService.checkAdmin(user))
                return productService.searchProduct(productName, productId, pageNum, pageSize);
            else
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_ADMIN.getCode()
                        , "Permission deny");
        } else
            return ServerResponse.createByErrorMessage("Need login");
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false)
                                             MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+ "/img/" + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }
}
