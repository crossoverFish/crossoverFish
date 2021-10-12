package com.tyu.common.constant;

/**
 * 常量属性
 * 
 * @author crossoverFish
 */
public class Constant {

	private Constant() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 商户申请的访问KEY
	 */
	public static final String ACCESS_KEY_ID = "accessKeyId";

	/**
	 * 商户访问签名参数
	 */
	public static final String HEADER_SIGN = "sign";

	/**
	 * 请求中 ACCESS_TOKEN 标志
	 */
	public static final String ACCESS_TOKEN = "token";


	/**
	 * 活动状态(0:无效;1:有效)
	 */
	public interface ActivityStatus {
		String NO_ACTIVE = "0";
		String IS_ACTIVE = "1";
	}

}
