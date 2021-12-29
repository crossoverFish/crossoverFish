package com.tyu.web.core.config;

import com.tyu.web.core.handler.RequestInterceptor;
import com.tyu.web.core.handler.UserPrincipalMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 自定义API-MVC配置, 继承自适配器
 * 
 * @author crossoverFish
 */
@Configuration
public class ApiMvcConfiguration implements WebMvcConfigurer {

	@Autowired
	private UserPrincipalMethodArgumentResolver userPrincipalMethodArgumentResolver;


	@Autowired
	private RequestInterceptor requestInterceptor;



	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestInterceptor).addPathPatterns("/**");
	}


	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// 自定义用户信息解析器
		argumentResolvers.add(userPrincipalMethodArgumentResolver);
	}

}
