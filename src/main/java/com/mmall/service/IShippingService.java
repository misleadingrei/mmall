package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * Created by misleadingrei on 12/15/17.
 */
public interface IShippingService {
    ServerResponse add (Integer userId, Shipping shipping);
    ServerResponse delete(Integer userId,Integer shippingId);
    ServerResponse update(Integer userId,Shipping shipping);
    ServerResponse<Shipping> select(Integer userId,Integer shippingId);
    ServerResponse<PageInfo> list(Integer pageNum , Integer pageSize , Integer userId);
}
