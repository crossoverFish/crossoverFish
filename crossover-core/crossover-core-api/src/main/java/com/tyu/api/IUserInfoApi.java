package com.tyu.api;

import com.tyu.common.exception.BusinessException;
import com.tyu.core.model.UserPrincipalVO;

public interface IUserInfoApi {

     int registerUser(UserPrincipalVO userPrincipalVO);


    String login(UserPrincipalVO userPrincipalVO) throws BusinessException;

}
