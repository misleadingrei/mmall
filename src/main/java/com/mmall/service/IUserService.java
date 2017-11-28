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
    ServerResponse<User> getUserInfo(HttpSession session);
    ServerResponse<String> getQuestionByUsername(String username);
    ServerResponse<String> forgetCheckAnswer (String username ,String question ,String answer);
}
