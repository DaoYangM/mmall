package com.mmall.service.impl;

import com.mmall.aop.UserLoginAOP;
import com.mmall.common.Const;
import com.mmall.common.NeedLogin;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.*;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLoginAOP loginAOP;

    @Autowired
    private HttpSession session;

    @Override
    public boolean checkAdmin(User user) {

        if (user.getRole() == Const.ROLE.ADMIN)
            return true;
        return false;
    }

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount =  validate(username, Const.TYPE.USERNAME);
        if (resultCount == 0)
            return ServerResponse.createByErrorMessage("User doesn't exist");

        User user = userMapper.selectLogin(username, MD5Util.MD5EncodeUtf8(password));
        if (user == null)
            return ServerResponse.createByErrorMessage("Password Error");

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("Login Success", user);
    }

    @Override
    public ServerResponse<Integer> logout(HttpSession httpSession) {
        httpSession.removeAttribute(Const.CURRENT_USER);

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<Integer> register(String username, String password, String email,
                                   String phone, String question, String answer) {
        int resultUsernameCount = validate(username, Const.TYPE.USERNAME);
        if (resultUsernameCount != 0)
            return ServerResponse.createByErrorMessage("User has existed");

        if (validate(email, Const.TYPE.EMAIL) != 0)
            return ServerResponse.createByErrorMessage("Email has existed");
        if (username == null || password == null)
            return ServerResponse.createByErrorMessage("Username or password can't be null");
        User user = new User();
        user.setUsername(username);
        user.setPassword(MD5Util.MD5EncodeUtf8(password));
        user.setEmail(email);
        user.setPhone(phone);
        user.setQuestion(question);
        user.setAnswer(answer);
        user.setRole(Const.ROLE.NORMAL);
        int insertResult = userMapper.insert(user);
        if (insertResult == 1) {
            return ServerResponse.createBySuccess("Insert user success", 1);
        } else {
            return ServerResponse.createByError();
        }
    }

    @Override
    @NeedLogin
    public ServerResponse<User> getUserInfo(HttpSession session) {

       User user = loginAOP.getUser();
       if (user != null) {
           return  ServerResponse.createBySuccess("fetch info success", user);
       } else
           return ServerResponse.createByErrorMessage("fetch info failure");
    }

    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        String question;
        if (validate(username, Const.TYPE.USERNAME) > 0) {
            question = userMapper.getQuestion(username);
            if (question != null) {
                return ServerResponse.createBySuccessMessage(question);
            } else {
                return ServerResponse.createByErrorMessage("Question is empty");
            }
        } else
            return ServerResponse.createByErrorMessage("Fetching Question is failure");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String questin, String answer) {
        if (validate(username, Const.TYPE.USERNAME) == 0)
            return ServerResponse.createByErrorMessage("username doesn't exist");
        String DBanswer = userMapper.getAnswer(username);
        if (StringUtils.isNotBlank(DBanswer)) {
            if (DBanswer.equals(answer)) {
                String uuid = UUID.randomUUID().toString();
                TokenCache.setKey(Const.TOKENPREFIX+username, uuid);

                return ServerResponse.createBySuccessMessage(uuid);
            }
            return ServerResponse.createByErrorMessage("Answer doesn't correct");
        }
        return ServerResponse.createByErrorMessage("Answer is empty");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String token) {
        if (validate(username, Const.TYPE.USERNAME) > 0) {
               String userToken = TokenCache.getKey(Const.TOKENPREFIX+username);
            if (userToken != null && userToken.equals(token)) {
                User user = userMapper.getUserByUsername(username);
                user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
                int result = userMapper.updateByPrimaryKey(user);
                if (result == 1)
                    return ServerResponse.createBySuccessMessage("Updating user password success!");
                return ServerResponse.createByErrorMessage("Updating user password failure!");
            } else
                return ServerResponse.createByErrorMessage("He He");
        } else {
            return ServerResponse.createByErrorMessage("username doesn't exist");
        }
    }

    @Override
    public ServerResponse<String> resetPassword(HttpSession session, String oldPassword, String newPassword) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            User user1 = userMapper.selectByPrimaryKey(user.getId());
            if (user1.getPassword().equals(MD5Util.MD5EncodeUtf8(oldPassword))){
                if (StringUtils.isNotBlank(newPassword)) {
                    user1.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
                   int result = userMapper.updateByPrimaryKey(user1);
                   if (result == 1) {
                       return ServerResponse.createBySuccessMessage("Updating password success!");
                   }
                    return ServerResponse.createByErrorMessage("Updating password error");
                } else {
                    return ServerResponse.createByErrorMessage("New password is not empty!");
                }
            } else {
                return ServerResponse.createByErrorMessage("Old password is not correct!");
            }
        } else {
            return ServerResponse.createByErrorMessage("Please login first!");
        }
    }

    @Override
    public int updateInformation(User user) {
        int result = userMapper.updateInformation(user);

        if (result == 1)
            return 1;

        return 0;
    }

    @Override
    public ServerResponse<User> getInformation(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        user1.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("Get information success!", user1);
    }

    public int validate(String str, String type) {
        int result = 0;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(str)) {
            switch (type) {
                case Const.TYPE.USERNAME:
                    result = userMapper.checkUseraame(str);
                    break;

                case Const.TYPE.EMAIL:
                    result = userMapper.checkEmail(str);
                    break;
            }
            return result;
        } else {
            return -1;
        }
    }
}
