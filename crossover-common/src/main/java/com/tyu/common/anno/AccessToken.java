package com.tyu.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为私有API接口提供token验证
 * 
 * <pre>
 * 1. 在需要签名的API方法上加上此注解 
 * 2. 直接在API类上使用,代表该API类的所有方法均需要签名
 * 3. 直接在API类上使用,如果类中有方法不需要签名则hasToken参数置为False
 * </pre>
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessToken {

	/**
	 * 是否需要token认证
	 * 
	 * @return
	 */
	boolean hasToken() default true;

}