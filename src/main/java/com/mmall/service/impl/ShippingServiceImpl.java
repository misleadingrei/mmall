package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by misleadingrei on 12/15/17.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    //return hashmap contains shipping id of this userId added
    public ServerResponse add (Integer userId,Shipping shipping){
        //front-end cant't get userId which is in session
        // so we should fill iup userId by ourselves
        shipping.setUserId(userId);
        int rowCount=(shippingMapper.insert(shipping));
        if(rowCount==0)
            return ServerResponse.createByErrorMsg("insert failed");
        // not zero
        Map<String,Integer>  map = new HashMap<>();
        map.put("shipping",shipping.getId());
        return ServerResponse.createBySuccess("insert success",map);
    }

    //use userId and shippingId to prevent security problem ;
    public  ServerResponse delete(Integer userId,Integer shippingId){
        int rowCount=shippingMapper.deleteByPrimaryKeyAndUserId(shippingId,userId);
        if(rowCount==0)
            return ServerResponse.createByErrorMsg("delete failed");
        // not zero
        return ServerResponse.createBySuccessrMsg("delete success");
    }


    //front-end knows shippingId in update sutiation
    //must set userId in shipping manually
    //because this value in shipping can be setted  by others
    public  ServerResponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByPrimaryKeyAndUserIdSelective(shipping);
        if(rowCount==0)
            return ServerResponse.createByErrorMsg("update failed");
        // not zero
        return ServerResponse.createBySuccessrMsg("update  success");
    }

    public  ServerResponse<Shipping> select(Integer userId,Integer shippingId){
       Shipping shipping =shippingMapper.selectByPrimaryKeyAndUserId(shippingId,userId);
        if(shipping==null)
            return  ServerResponse.createByErrorMsg("select failed");
        return ServerResponse.createBySuccessData(shipping);
    }

    //use pageHelper
    public  ServerResponse<PageInfo> list(Integer pageNum , Integer pageSize , Integer userId){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.listByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        pageInfo.setList(shippingList);
        return ServerResponse.createBySuccessData(pageInfo);
    }




}
