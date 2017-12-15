package com.mmall.service;

import com.mmall.VO.CartVo;
import com.mmall.common.ServerResponse;

/**
 * Created by misleadingrei on 12/13/17.
 */
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> update( Integer productId, Integer count,Integer userId);
    ServerResponse<CartVo> delete(String productIds ,Integer userId);
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> selectOrUnselectAll(Integer userId,Integer checked);
    ServerResponse<CartVo> selectOrUnselectAlone(Integer userId,Integer checked,Integer productId);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
