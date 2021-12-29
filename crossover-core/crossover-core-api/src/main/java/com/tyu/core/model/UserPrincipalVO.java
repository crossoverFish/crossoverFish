package com.tyu.core.model;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author crossoverFish
 */
@Data
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "UserPrincipal", description = "用户信息")
public class UserPrincipalVO implements Serializable {
	static {
		System.out.println("UserPrincipalVO");
	}

	@ApiModelProperty(value = "用户id")
	private Long id;

	@ApiModelProperty(value = "用户名称")
	private String username;

	@ApiModelProperty(value = "用户密码")
	private String password;

	@ApiModelProperty(value = "用户状态")
	private Integer active_status;

	@ApiModelProperty(value = "TOKEN")
	private String token;

	@ApiModelProperty(value = "是否记住我")
	private String isRememberMe;


	public static void main(String[] args) {
		UserPrincipalVO userPrincipalVO = new UserPrincipalVO();
		userPrincipalVO.setId(111111L);
		userPrincipalVO.setUsername("撒大大");
		userPrincipalVO.setPassword("abd!2123");
		String s = JSONObject.toJSONString(userPrincipalVO);
		System.out.println(s);
	}
}
