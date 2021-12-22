package com.tyu.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * springBoot应用入口
 * 
 * @author crossoverFish
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource({"classpath:/dubbo/*.xml"})
public class WebApp {


	public static void main(String[] args) {
		SpringApplication.run(WebApp.class, args);
	}

}
