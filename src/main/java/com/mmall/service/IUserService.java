package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

import javax.servlet.http.HttpSession;

public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<Integer> logout(HttpSession session);

    ServerResponse<Integer> register(String username, String password, String email,
                                   String phone, String question, String answer);

    ServerResponse<User> getUserInfo(HttpSession session);

    ServerResponse<String> forgetGetQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String questin, String answer);

    ServerResponse<String> forgetResetPassword(String username, String newPassword, String token);

    ServerResponse<String> resetPassword(HttpSession session, String oldPassword, String newPassword);

    int updateInformation(User user);

    ServerResponse<User> getInformation(User user);

    boolean checkAdmin(User user);

}
