package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by misleadingrei on 11/28/17.
 */
@Controller
@RequestMapping ("/manage/user")
public class UserManagerController {

    @Autowired private IUserService iUserServiceimpl;


    @RequestMapping(value ="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login (String username, String password, HttpSession session)
    {
        ServerResponse response=iUserServiceimpl.login(username,password);
        // login success
        if(response.isSuccess())
        {
            User user =(User) response.getData();

            //here it is int   don not need equals


            // admin
            if(Const.Role.ROLE_ADMIN==user.getRole())
            {
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }
            else
                return ServerResponse.createByErrorMsg("you are not admin");

        }
        //login failed
            return ServerResponse.createByErrorMsg("login failed");

    }

}
