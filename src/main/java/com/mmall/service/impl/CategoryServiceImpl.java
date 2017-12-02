package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by misleadingrei on 12/2/17.
 */
@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

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
            return ServerResponse.createByErrorMsg("wrong arg");

        Category category = new Category();
        category.setParentId(categoryId);
        category.setName(newName);

        int rowCount =categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount==0)
            return ServerResponse.createByErrorMsg("update failed");
        return ServerResponse.createBySuccessrMsg("update category succeed");


    }
}
