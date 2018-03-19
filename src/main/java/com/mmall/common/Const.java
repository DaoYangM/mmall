package com.mmall.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String TOKENPREFIX = "token_";

    public interface ROLE {
        int ADMIN = 1;
        int NORMAL = 0;
        int ERROR = 2;
    }

    public interface TYPE {
        String USERNAME = "username";
        String EMAIL = "email";
    }
}
