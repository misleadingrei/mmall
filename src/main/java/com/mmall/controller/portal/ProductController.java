package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.VO.ProductDetailsVo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by misleadingrei on 12/11/17.
 */
//beacuse this controller is uesd for portal to get product ,they don't have privileges to modify product
//which means read-only .so in this controller we don't check access at all.
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired private IProductService iProductService;


    @RequestMapping("details.do")
    @ResponseBody
    public ServerResponse<ProductDetailsVo> details (Integer productId){
        return iProductService.getProductDetails(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list (@RequestParam(value="keyword",required = false) String keyword,
                                          @RequestParam(value="categoryId",required = false) Integer categoryId ,
                                          @RequestParam(value="page{ize",defaultValue="1") int pageSize,
                                          @RequestParam(value="pageNum",defaultValue="10")int pageNum,
                                          @RequestParam(value = "orderBy",defaultValue = "")String orderBy){

        return iProductService.listProductByKeywordAndCategoryId(keyword,categoryId,pageSize,pageNum,orderBy);
    }

}
