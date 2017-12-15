package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByProductIdAndUserId(@Param("productId") Integer productId, @Param("userId") Integer userId);

    List<Cart> selectCartlistByUserId(Integer userId);

    int  selectProductUncheckStatusByUserId(Integer userId);

    int  deleteByProductIdsAndUserId(@Param("productIds") String productIds ,@Param("userId") Integer userId);

    int  checkOrUncheckProduct(@Param("userId") Integer userId, @Param("checked") Integer checked);

    int  checkOrUncheckProductAlone(@Param("userId") Integer userId, @Param("checked") Integer checked,@Param("productId")Integer productId);

    int selectCartProductCount(Integer userId);
}