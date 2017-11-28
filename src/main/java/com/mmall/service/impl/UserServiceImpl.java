package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmall.service.IUserService;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by misleadingrei on 11/26/17.
 */
/*
   implement UserService Interface
 */
@Service("iUserServiceImpl")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper ;

    /*
         login impl
     */
    public ServerResponse<User> login (String userName, String password)
    {
        int resultCount = userMapper.checkUserName(userName);
        // user doesn't exit
        if(resultCount==0)
        {
            return ServerResponse.createByErrorMsg("user name doesn't exist");

        }

        //todo passowrd md5
        String MD5password = MD5Util.MD5EncodeUtf8(password);

            User user = userMapper.selectLogin(userName,password);
            //wrong password
            if(user==null)
                return ServerResponse.createByErrorMsg("wrong password");

            //success ,then set password to emtpy when success login
            user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
            return ServerResponse.createBySuccess("login success",user);
    }

    /*
           log out impl
     */
    public ServerResponse<String> logout(HttpSession session)
    {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /*
          register impl
     */
    public ServerResponse<String> register(User user)
    {
        //check Username
        ServerResponse validResponse = checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess())
             return validResponse;

        //check Email
        validResponse=checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess())
            return validResponse;
        user.setRole(Const.Role.ROLE_CUSTOMER);

        // password with MD5

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount=userMapper.insert(user);

        if(resultCount==0)
            return ServerResponse.createByErrorMsg("reg failed");

        return ServerResponse.createBySuccessrMsg("reg successed");


    }

    /*
         check valid impl now it supprot only two values userName or Email
     */
    public ServerResponse<String> checkValid(String str ,String type)
    {
               if(org.apache.commons.lang3.StringUtils.isNotBlank(type))
               {
                   // start check

                   //check user
                   if(Const.USERNAME.equals(type))
                   {
                       int resultCount = userMapper.checkUserName(str);
                       // has same user
                       if(resultCount>0)
                           return ServerResponse.createByErrorMsg("user already exist");
                   }
                   // check Email
                  if(Const.EMAIL.equals(type))
                  {
                      int EmailCount = userMapper.checkEmail(str);
                      // has same email
                      if(EmailCount>0)
                          return ServerResponse.createByErrorMsg("Email already exist");
                  }
                   // input val is vaild
                   return ServerResponse.createBySuccessrMsg("check succeed,don't have a existed value");
               }
               // wrong args
              else
                   return ServerResponse.createByErrorMsg("wrong args");
    }

    /*
           get user info

     */
    public ServerResponse<User> getUserInfo(HttpSession session)
    {
           User user = (User)session.getAttribute(Const.CURRENT_USER);
           if(user!=null)
           return ServerResponse.createBySuccessData(user);
           else
           return ServerResponse.createByErrorMsg("user not login ");
    }

    /*
        get question by username
     */
    public ServerResponse<String> getQuestionByUsername(String username){
        ServerResponse validResponse = checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess())
            //dont't have a valid user
         return ServerResponse.createByErrorMsg("sorry , user does not exist");

         String result = userMapper.selectQuestionByUsername(username);
         if(!org.apache.commons.lang3.StringUtils.isNotBlank(result))
             return ServerResponse.createByErrorMsg("sorry,select user doesn't have a question or question is blank ");

        return ServerResponse.createBySuccessData(result);

    }
     /*
          check  input answer   by a specific username and question and answer in db .

          forget_check_answer
     */
    public  ServerResponse<String> forgetCheckAnswer (String username ,String question ,String answer){
        int reslutCount = userMapper.checkAnswer(username ,question ,answer);
        if(reslutCount>0)
            // input answer consiset with db answer then generate tokens
        {
            // non-repetable string -used for tokens
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);

            return ServerResponse.createBySuccessData(forgetToken);
        }
        return ServerResponse.createByErrorMsg("answer is incorrect");
    }
    /*
          gorget_rest_password impl
          forget_token is a user commit token for latter compare
     */
    public ServerResponse<String> forgetResetPassword(String username ,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken))
            return ServerResponse.createByErrorMsg("emtpy token");
        // because we use TokenCache.TOKEN_PREFIX+username as forgetToken here,
        // once user is blank token will be only TokenCache.TOKEN_PREFIX
        // it is not acceptable
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess())
            // user does not existed
        {
            return ServerResponse.createByErrorMsg("username error");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token))
        {
            return ServerResponse.createByErrorMsg("wrong forget token or expired token");
        }
        if(forgetToken.equals(token))
        {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount =userMapper.updatemd5PasswordByUsername(username,md5Password);

            if(rowCount>0)
                return ServerResponse.createBySuccessrMsg("modify successfully");
            else
                return ServerResponse.createByErrorMsg("modify failed");
        }
        else
        {
            return ServerResponse.createByErrorMsg("wrong token please please renew token");
        }
    }
    /*
            login reset passsword
     */
    public ServerResponse<String>  resetPassword(String passwordOld,String passwordNew,User user){
          //must check passwordOld with user
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        //old password incorrect
        if(resultCount==0)
            return ServerResponse.createByErrorMsg("incorrect old password");
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));

        resultCount = userMapper.updateByPrimaryKeySelective(user);

        if(resultCount==0)
            return ServerResponse.createByErrorMsg("update password failed");

        return ServerResponse.createBySuccessrMsg("update password successfully");

    }
   /*
        update  info impl
    */
    public ServerResponse<User> updateUserInfo(User user){
             //username can't be update and email need to bc checked
        //check email use checkEmailByUserId to ensure the updated email is not used
        //by other users
        int resultCount = userMapper.checkEmailByUserId(user.getId(),user.getEmail());
        if(resultCount>0){
           return ServerResponse.createByErrorMsg("email has existed please change you email");
        }

        //use this only for update user because we can't update userName
        User updateUser = new User();
        //set update user don't update update_time because it is implemented in the db
        updateUser.setId(user.getId());
        updateUser.setPassword(user.getPassword());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());


        resultCount=userMapper.updateByPrimaryKeySelective(user);
        if(resultCount>0)
            return ServerResponse.createBySuccess("update succeed",updateUser);
        return ServerResponse.createByErrorMsg("failed to update info");
    }
}
