package com.tyu.common.constant;

import java.util.HashMap;
import java.util.Map;

public class SignTokenConstant {
    public static final Map<String, String> PARTNERS = new HashMap<>();

    public static String ACCESS = "access1";

    /**
     * 暂时只发放2b2c的accessKeyId和accessKeySecret
     */
    static {
        // key is accessKeyId, value is accessKeySecret
        PARTNERS.put(ACCESS, "access1@1234");
    }


    /**
     *  访问KEY
     */
    public static final String ACCESS_KEY_ID = "accessKeyId";


    /**
     * 访问签名参数
     */
    public static final String HEADER_SIGN = "sign";


    /**
     * 访问签名时间
     */
    public static final String HEADER_TIME = "time";


    /**
     * token前缀
     */
    public static String TOKEN_PREFIX = "Bearer ";


    /**
     * 存进客户端的token的key名
     */
    public static final String USER_LOGIN_TOKEN = "token";

}
