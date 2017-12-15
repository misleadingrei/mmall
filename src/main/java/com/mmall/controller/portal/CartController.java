package com.mmall.controller.portal;


import com.mmall.VO.CartVo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by misleadingrei on 12/13/17.
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping(value="add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);

    }

    @RequestMapping(value="add.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer productId, Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);

    }


    @RequestMapping(value="delete.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String productIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.delete(productIds,user.getId());

    }

    @RequestMapping(value="list.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.list(user.getId());

    }


    @RequestMapping(value="select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrUnselectAll(user.getId(),Const.productCheck.CHECKED);

    }

    @RequestMapping(value="unselect_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unselect(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrUnselectAll(user.getId(),Const.productCheck.UNCHECKED);

    }

    @RequestMapping(value="select_alone.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAlone(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrUnselectAlone(user.getId(),Const.productCheck.CHECKED,productId);

    }

    @RequestMapping(value="unselect_alone.do")
    @ResponseBody
    public ServerResponse<CartVo> unselect(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrUnselectAlone(user.getId(),Const.productCheck.UNCHECKED,productId);

    }


    @RequestMapping(value="get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //if user not login
        //we return 0 instead of an error
        if(user==null) {
            return ServerResponse.createBySuccessData(0);
        }
        return iCartService.getCartProductCount(user.getId());

    }
}
