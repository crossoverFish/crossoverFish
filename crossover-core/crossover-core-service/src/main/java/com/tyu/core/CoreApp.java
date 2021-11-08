package com.tyu.core;

import com.tyu.core.listener.ApplicationStartup;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * springBoot应用入口
 * 
 * @author crossoverFish
 */
@SpringBootApplication
@ImportResource({"classpath:/dubbo/*.xml"})
@MapperScan("com.tyu.core.dao")
public class CoreApp {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(CoreApp.class);
		application.addListeners(new ApplicationStartup());
		application.run(args);
	}

}
