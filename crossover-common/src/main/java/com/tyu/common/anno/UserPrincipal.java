package com.tyu.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UserPrincipal Annotation 在登录后需要获取用户信息的API方法参数内加上此注解
 * 
 * @author crossoverFish
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPrincipal {
}