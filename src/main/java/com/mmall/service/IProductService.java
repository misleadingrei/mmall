package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.VO.ProductDetailsVo;
import com.mmall.VO.ProductListVo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by misleadingrei on 12/7/17.
 */
public interface IProductService {
     ServerResponse insertOrUpdateProduct(Product product);
     ServerResponse setSaleStatus(Integer productId,Integer status);
     ServerResponse<ProductDetailsVo> manageProductDetails(Integer productId);
     ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
     ServerResponse<PageInfo>  searchProduct (String productName,Integer productId,int pageNum, int pageSize);
}
