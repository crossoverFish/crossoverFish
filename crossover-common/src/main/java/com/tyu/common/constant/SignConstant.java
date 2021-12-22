package com.tyu.common.constant;

public class SignConstant {
    private SignConstant() {
        throw new IllegalStateException("Utility class");
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
}
