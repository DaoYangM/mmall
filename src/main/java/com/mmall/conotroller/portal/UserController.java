package com.mmall.conotroller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession httpSession) {
        // service -> mybatis
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            httpSession.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Integer> logout(HttpSession session){
        return iUserService.logout(session);
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> register(String username, String password, String email,
                                   String phone, String question, String answer) {

        ServerResponse<Integer> serverResponse = iUserService.register(username, password, email, phone, question, answer);
        return serverResponse;
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        return iUserService.getUserInfo(session);
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.forgetGetQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        String clientIp = httpServletRequest.getRemoteAddr();

        if(TokenCache.getKey(clientIp) == null) {
            TokenCache.setKey(clientIp, "1");
        } else {
            TokenCache.setKey(clientIp, String.valueOf(Integer.parseInt(TokenCache.getKey(clientIp))+ 1));
            if (Integer.parseInt(TokenCache.getKey(clientIp)) >=3 )
                return ServerResponse.createBySuccessMessage("Byebye");
        }

        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String token) {
        return iUserService.forgetResetPassword(username, newPassword, token);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String oldPassword, String newPassword) {
        return iUserService.resetPassword(session, oldPassword, newPassword);
    }

    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        user.setId(currentUser.getId());
        if (currentUser != null) {
            if (iUserService.updateInformation(user) == 1) {
                return ServerResponse.createBySuccess("Updating information success!", user);
            }
        }
        return ServerResponse.createByErrorMessage("Please login first");
    }

    @RequestMapping(value = "get_information.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Need login");
        }
        return iUserService.getInformation(user);
    }
}
