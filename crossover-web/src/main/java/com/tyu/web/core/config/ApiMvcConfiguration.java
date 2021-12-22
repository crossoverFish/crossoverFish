package com.tyu.web.core.config;

import com.tyu.web.core.handler.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义API-MVC配置, 继承自适配器
 * 
 * @author crossoverFish
 */
@Configuration
public class ApiMvcConfiguration implements WebMvcConfigurer {



	@Autowired
	private RequestInterceptor requestInterceptor;



	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestInterceptor).addPathPatterns("/**");
	}

}
