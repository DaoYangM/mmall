package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String TOKENPREFIX = "token_";

    public interface ProductOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public interface Cart {
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";

        Integer CHECKED = 1;
        Integer UNCHECKED = 0;
    }

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
