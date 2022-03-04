package com.tyu.api;

import com.tyu.common.exception.BusinessException;
import com.tyu.core.model.UserPrincipalVO;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

public interface IUserInfoApi {

     int registerUser(UserPrincipalVO userPrincipalVO);


    String login(UserPrincipalVO userPrincipalVO) throws BusinessException;

    void testTransaction();

    @Transactional(rollbackFor = Exception.class,propagation = REQUIRES_NEW)
    void sq2();
}
