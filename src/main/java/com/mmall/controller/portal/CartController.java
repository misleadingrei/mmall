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

    @RequestMapping(value="add.do" ,method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc())
        }
        return iCartService.add(user.getId(),productId,count);

    }
}