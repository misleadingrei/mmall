package com.mmall.service;

import com.mmall.VO.CartVo;
import com.mmall.common.ServerResponse;

/**
 * Created by misleadingrei on 12/13/17.
 */
public interface ICartService {
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
}
