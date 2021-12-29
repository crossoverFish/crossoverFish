package com.tyu.web.controller;

import com.tyu.core.model.UserPrincipalVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @Author: crossoverFish
 * @Description: 基类
 * @ModifiedBy:
 **/
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected HttpServletRequest request;

    /**
     * 获取登录用户手机号
     *
     * @return
     */
    public UserPrincipalVO getLoginUser() {
        return Optional.ofNullable((UserPrincipalVO)request.getAttribute("userInfo")).orElse(null);
    }

}
