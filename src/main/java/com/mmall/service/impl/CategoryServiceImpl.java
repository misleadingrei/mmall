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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    public ServerResponse  getChildrenParallelCategory (Integer parentId){
        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(parentId);
        if(CollectionUtils.isEmpty(list)){
            logger.info("child of this category is empty");
        }
        return ServerResponse.createBySuccessData(list);
    }

    public ServerResponse getCategoryAndDeepChildrenCategory(Integer categoryId){
          Set<Category> set= new HashSet<>();
          return getCategoryAndDeepChildrenCategoryRaw(categoryId,set);

    }

    public ServerResponse getCategoryAndDeepChildrenCategoryRaw (Integer categoryId,Set<Category> set){
        ServerResponse response = getChildrenParallelCategory(categoryId);
        List<Category> list = ( List<Category>) response.getData();
        if(CollectionUtils.isEmpty(list))
            return ServerResponse.createBySuccessrMsg("end of child");
        // add list to set for trim
        set = trimChildCategory(list,set);
        for(Category category :list){
            getCategoryAndDeepChildrenCategoryRaw(category.getId(),set);
        }
        return ServerResponse.createBySuccessData(set);
    }


    // to impl set unqie
    //we need to overwrite Category's hashcode() and equals
    public Set<Category> trimChildCategory(List<Category> list,Set<Category> set){
        for(Category category :list){
            if(!set.contains(category))
                set.add(category);
        }
        return set;
    }

}
