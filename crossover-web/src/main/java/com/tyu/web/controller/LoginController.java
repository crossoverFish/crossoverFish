package com.tyu.web.controller;

import com.tyu.api.IUserInfoApi;
import com.tyu.common.enums.MsgEnum;
import com.tyu.common.exception.SystemException;
import com.tyu.common.model.Response;
import com.tyu.core.model.UserPrincipalVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@Api(tags = "用户相关接口")
@RestController
@RequestMapping(value = "/user")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Autowired
    private IUserInfoApi userInfoApi;

    @ApiOperation(value = "注册", notes = "注册用户成功返回200")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserPrincipalVO userPrincipalVO){
        int state = userInfoApi.registerUser(userPrincipalVO);
        if (state > 0) {
            return ResponseEntity.ok(MsgEnum.SUCCESS.msg("注册用户成功"));
        }
        throw new SystemException();
    }

    @ApiOperation(value = "登录", notes = "获取资源成功响应200,资源若不存在则响应404")
	@PostMapping("/login")
	public Response login(@RequestBody UserPrincipalVO userPrincipalVO, HttpServletResponse response) {
        String token = userInfoApi.login(userPrincipalVO);
        response.addHeader("USER_LOGIN_TOKEN",token);
        return Response.success();
	}

}
