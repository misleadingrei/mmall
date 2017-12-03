package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import com.sun.corba.se.spi.activation.Server;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by misleadingrei on 12/2/17.
 */
@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    //use this logger when return list is empty see below
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /*
         addCategory impls
     */
    public ServerResponse addCategory (String categoryName, Integer parentId){
        // args chek
         if(parentId==null|| StringUtils.isBlank(categoryName))
             return ServerResponse.createByErrorMsg("wrong arg");
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        //is valid
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if(rowCount==0)
            return ServerResponse.createByErrorMsg("insert failed");
        return ServerResponse.createBySuccessrMsg("add category succeed");
    }

    /*
         update category name impl
     */
    public ServerResponse updateCategoryName(String newName ,Integer categoryId){
        // args check
        if(categoryId==null|| StringUtils.isBlank(newName))
        {
            return ServerResponse.createByErrorMsg("wrong arg");
        }

        Category category = new Category();
        category.setParentId(categoryId);
        category.setName(newName);

        int rowCount =categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount==0)
            return ServerResponse.createByErrorMsg("update failed");
        return ServerResponse.createBySuccessrMsg("update category succeed");


    }


    public ServerResponse  getChildParallelCategory (Integer categoryId){
        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(list)){
            logger.info("child of this category is empty");
        }
        return ServerResponse.createBySuccessData(list);
    }

    public ServerResponse getCategoryAndDeepChildrenCategory(Integer categoryId){

    }

}
