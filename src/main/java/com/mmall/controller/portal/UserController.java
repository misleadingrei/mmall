package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mmall.service.IUserService;

import javax.servlet.http.HttpSession;

/**
 * Created by misleadingrei on 11/26/17.
 */

/*
    attention : session will not be passed th service
 */
@Controller
@RequestMapping("/user")
public class UserController {
    /*
         user login
     */
    @Autowired private IUserService iUserServiceimpl;
    @RequestMapping(value="login.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login (String username , String password , HttpSession session)
    {
              ServerResponse<User> response =  iUserServiceimpl.login(username,password);
              if(response.isSuccess())
              {
                  session.setAttribute(Const.CURRENT_USER,response.getData());
              }
              return response;
    }
    /*
       user logout
     */

    @RequestMapping(value="logout.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session)
    {
                ServerResponse<String> response =  iUserServiceimpl.logout(session);
                return response;
    }


    /*
          register
     */

    @RequestMapping(value="register.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user)
    {
             return iUserServiceimpl.register(user);
    }

    /*
        checkVaild
     */
    @RequestMapping(value="checkValid.do",method= RequestMethod.POST)
    @ResponseBody public ServerResponse<String> checkValid(String str,String type) {return iUserServiceimpl.checkValid(str,type);}


    /*
        get_user_info
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session)
    {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user!=null)
            return ServerResponse.createBySuccessData(user);
        else
            return ServerResponse.createByErrorMsg("user not login ");
    }


    /*
       select username by username
       when forget password program uses password question  return answer of the question
    */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> selectQuestionByUsername(String username){
            return iUserServiceimpl.getQuestionByUsername(username);
    }

    /*
           we return tokens in the String in the ServerResponse<String> for latter reset use
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer (String username ,String question ,String answer)
    {
                return iUserServiceimpl.forgetCheckAnswer(username,question,answer);
    }
    /*
           forget_reset_password controller
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username ,String passwordNew,String forgetToken){
               return iUserServiceimpl.forgetResetPassword(username,passwordNew,forgetToken);
    }
    /*
         login reset passsword controller
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String>  resetPassword(String passwordOld,String passwordNew,HttpSession session){
            User user = (User) session.getAttribute(Const.CURRENT_USER);
           if(user==null){
               return ServerResponse.createByErrorMsg("user not login ");
           }
           return iUserServiceimpl.resetPassword(passwordOld,passwordNew,user);

    }
    /*
          update info controller
     */
    @RequestMapping(value = "update_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo (HttpSession session ,User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorMsg("user not login ");
        }
        // copy current user id in the session to the user we get
        //id and username cant' be updated so we replace them in the update user
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response=iUserServiceimpl.updateUserInfo(user);
        if(response.isSuccess())
            session.setAttribute(Const.CURRENT_USER,response.getData());
        return response;

    }
    /*
       if this controller is called when user does not login
       user will be required to login at first
     */
    @RequestMapping(value = "get_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation (HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"need force login");
        }
        return iUserServiceimpl.getInformation(currentUser.getId());

    }

}
