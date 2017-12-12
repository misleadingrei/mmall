package com.mmall.service.impl;

import com.github.orderbyhelper.sqlsource.OrderByDynamicSqlSource;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.VO.ProductDetailsVo;
import com.mmall.VO.ProductListVo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by misleadingrei on 12/7/17.
 */
@Service("IProductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;


    // this method is used for assemble Product Details VO
    private ProductDetailsVo assembleProductDetailsVo (Product product){
        ProductDetailsVo productDetailsVo = new ProductDetailsVo();

        //assemble
        productDetailsVo.setId(product.getId());
        productDetailsVo.setSubImages(product.getSubImages());
        productDetailsVo.setMainImage(product.getMainImage());
        productDetailsVo.setSubtitle(product.getSubtitle());
        productDetailsVo.setPrice(product.getPrice());
        productDetailsVo.setCategoryId(product.getCategoryId());
        productDetailsVo.setDetail(product.getDetail());
        productDetailsVo.setStatus(product.getStatus());
        productDetailsVo.setName(product.getName());
        productDetailsVo.setStock(product.getStock());

        //image hpst
        productDetailsVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));


        // category id

        Category category = categoryMapper.selectByPrimaryKey(product.getId());
        if(category==null){
            //null ---> root ---> zero
            productDetailsVo.setParentCategoryId(0);
        }
        productDetailsVo.setParentCategoryId(category.getId());

        //create_time
        //update_time

        productDetailsVo.setCreateTime(DateTimeUtil.DataToStr(product.getCreateTime()));
        productDetailsVo.setUpdateTime(DateTimeUtil.DataToStr(product.getUpdateTime()));


        return productDetailsVo;
    }


    public ServerResponse insertOrUpdateProduct(Product product){
          if(product==null){
              return ServerResponse.createByErrorMsg("Illegal Argument");
          }
        //not null
        //check image
        if(StringUtils.isNotBlank(product.getSubImages()))
        {
            // contact split rules with front-end
            String[] subImageArray = product.getSubImages().split(",");
            // use first sub image as main image
            if(subImageArray.length>0)
                product.setMainImage(subImageArray[0]);


        }
        Product result = productMapper.selectByPrimaryKey(product.getId());
        //insert
          if(result==null){
              int resultCount = productMapper.insert(product);
              if(resultCount==0)
                  return ServerResponse.createByErrorMsg("insert failed");
          }
          //update
        else{
              int resultCount = productMapper.updateByPrimaryKeySelective(product);
              if(resultCount==0)
                  return ServerResponse.createByErrorMsg("update failed");
          }
        return ServerResponse.createBySuccessrMsg("successful");
    }




    public ServerResponse setSaleStatus(Integer productId,Integer status){
         if(status==null||productId==null) return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
         Product tempProduct = new Product();

         tempProduct.setId(productId);
         tempProduct.setStatus(status);


        int rowCount = productMapper.updateByPrimaryKeySelective(tempProduct);

        if(rowCount==0)
            return  ServerResponse.createByErrorMsg("set status failed");

        return ServerResponse.createBySuccessrMsg("successfully");
    }



    public  ServerResponse<ProductDetailsVo> manageProductDetails( Integer productId){
        if(productId==null) return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        Product result = productMapper.selectByPrimaryKey(productId);
        if(result==null) return ServerResponse.createByErrorMsg("product does not exist");

        // value object
        ProductDetailsVo productDetailsVo = this.assembleProductDetailsVo(result);

       return ServerResponse.createBySuccessData(productDetailsVo);

    }

    public  ServerResponse<PageInfo> getProductList(int pageNUm, int pageSize){
        //before sql process
        PageHelper.startPage(pageNUm,pageSize);
        // sql process
        List<Product> productList = productMapper.selectProductList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product :productList){
            productListVoList.add(assembleProductListVo(product));
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccessData(pageResult);
    }

    //this method is used for assemble productListVo
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setId(product.getId());
        productListVo.setStatus(product.getStatus());
        productListVo.setPrice(product.getPrice());

        return productListVo;

    }


    public  ServerResponse<PageInfo> searchProduct (String productName,Integer productId,int pageNum, int pageSize){
          if(StringUtils.isBlank(productName))
          {
              // use this as regular matching
              productName = new StringBuilder().append("%").append(productName).append("%").toString();
          }


        //query by name and id
        List<Product>  resultList = productMapper.selectByNameAndProductId(productName,productId);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product :resultList){
            productListVoList.add(assembleProductListVo(product));
        }
        PageInfo pageResult = new PageInfo(resultList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccessData(pageResult);
    }


    /*
          following parts are impls of protal product use
     */
    public  ServerResponse<ProductDetailsVo> getProductDetails( Integer productId){
        if(productId==null) return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        Product result = productMapper.selectByPrimaryKey(productId);
        if(result==null) return ServerResponse.createByErrorMsg("product not for sale or  does not exist");
        if(result.getStatus()!= Const.ProductStatusEnum.On_SALE.getCode())
            return ServerResponse.createByErrorMsg("product not for sale or  does not exist ");
        // value object
        ProductDetailsVo productDetailsVo = this.assembleProductDetailsVo(result);

        return ServerResponse.createBySuccessData(productDetailsVo);

    }

    public ServerResponse<PageInfo> listProductByKeywordAndCategoryId(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
           //judge the correctness of arugments
        if(StringUtils.isBlank(keyword)&&categoryId==null)
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        // use this to store all sub category of this category and itself;
       List<Integer> categoryIdList = new ArrayList<Integer>();



        if(categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&StringUtils.isBlank(keyword))
                // if it is no category and keyword is blank,
               //return empty result
            {
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> listVos = new ArrayList<>();
                PageInfo pageInfo = new PageInfo(listVos);
                return ServerResponse.createBySuccessData(pageInfo);
            }
        }
        // now this categoryid is not null
        categoryIdList=iCategoryService.getCategoryAndDeepChildrenCategory(categoryId).getData();

        // keyword is not blank
        if(StringUtils.isNotBlank(keyword)){
            keyword= new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        //odered data
        PageHelper.startPage(pageNum,pageSize);

        //orderBy field not empty
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.productListOrderBy.PRICE_ASC_DESC.contains(orderBy));
            {
                String[] orderByArray = orderBy.split("_");
                // parse rule of pageHelper orderBy
                PageHelper.orderBy(orderByArray[0]+" " +orderByArray[1]);

            }
        }


        //to ensure the sql process is correct
        List<Product> productList = productMapper.selectByNameAndCategoryIdList(StringUtils.isBlank(keyword)?null:keyword,
                categoryIdList.size()==0?null:categoryIdList);
        //assemble product to productListVo
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem:productList){
            productListVoList.add(assembleProductListVo(productItem));
        }
        //start pag\ing

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccessData(pageInfo);



    }
}
