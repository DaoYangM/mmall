package com.mmall.conotroller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
@ResponseBody
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    public ServerResponse<ProductDetailVo> detail(Integer id) {
        if (id != null) {
            ServerResponse<ProductDetailVo> productDetailVoServerResponse = productService.manageProductDetail(id);
            return productDetailVoServerResponse;
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                         @RequestParam(value = "categoryId", defaultValue = "") Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {

        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
