package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by misleadingrei on 11/26/17.
 */
public interface IUserService {
    ServerResponse<User> login (String userName, String password);
    ServerResponse<String> logout(HttpSession session);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str ,String type);
    ServerResponse<String> getQuestionByUsername(String username);
    ServerResponse<String> forgetCheckAnswer (String username ,String question ,String answer);
    ServerResponse<String> forgetResetPassword(String username ,String passwordNew,String token);
    ServerResponse<String>  resetPassword(String passwordOld,String passwordNew,User user);
    ServerResponse<User> updateUserInfo (User user);
    ServerResponse<User> getInformation (Integer userId);
    ServerResponse<String> checkAdminRole(User user);
}
