package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Created by misleadingrei on 12/2/17.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(String newName ,Integer categoryId);
}
