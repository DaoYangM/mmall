package com.mmall.aop;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.service.impl.UserServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;

@Aspect
public class UserLoginAOP {

    private boolean isSuccess;

    private User user;

    @Pointcut(value = "@annotation(com.mmall.common.NeedLogin)")
    private void userlogin() {}
    @Before("userlogin())")
    public void checkLogin(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        HttpSession session = (HttpSession) args[0];
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            isSuccess = true;
            this.user = user;
        } else {
            isSuccess = false;
            this.user = null;
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public User getUser() {
        return user;
    }
}
