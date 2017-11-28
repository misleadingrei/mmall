package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
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
@Controller
@RequestMapping("/user/")
public class UserController {
    /*
         user login
     */
    @Autowired IUserService iUserServiceimpl;
    @RequestMapping(value="login.do",method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login (String userName , String password , HttpSession session)
    {
              ServerResponse<User> response =  iUserServiceimpl.login(userName,password);
              if(response.isSuccess())
              {
                  session.setAttribute(Const.CURRENT_USER,response.getData());
              }
              return response;
    }
    /*
       user logout
     */

    @RequestMapping(value="logout.do",method= RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session)
    {
                ServerResponse<String> response =  iUserServiceimpl.logout(session);
                return response;
    }




    /*
          register
     */

    @RequestMapping(value="register.do",method= RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user)
    {
             return iUserServiceimpl.register(user);
    }

    /*
        checkVaild
     */
    @RequestMapping(value="checkVaild.do",method= RequestMethod.GET)
    @ResponseBody public ServerResponse<String> checkValid(String str,String type) {return iUserServiceimpl.checkValid(str,type);}


    /*
        get_user_info
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session)
    {
         return iUserServiceimpl.getUserInfo(session);
    }


    /*
       select username by username
       when forget password program uses password question
    */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> selectQuestionByUsername(String username){
            return iUserServiceimpl.getQuestionByUsername(username);
    }

    /*
           we store tokens in the String in the ServerResponse<String> for latter reset use
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer (String username ,String question ,String answer)
    {
                return iUserServiceimpl.forgetCheckAnswer(username,question,answer);
    }
    /*
           forget
     */
}
