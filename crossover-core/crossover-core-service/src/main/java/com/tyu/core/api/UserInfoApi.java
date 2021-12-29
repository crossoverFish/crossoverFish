package com.tyu.core.api;

import com.tyu.api.IUserInfoApi;
import com.tyu.common.constant.SignTokenConstant;
import com.tyu.common.util.*;
import com.tyu.core.model.UserPrincipalVO;
import com.tyu.core.dao.UserInfoMapper;
import com.tyu.common.constant.Constant;
import com.tyu.common.exception.BusinessException;
import com.tyu.common.exception.NotFoundException;
import com.tyu.core.model.UserInfo;
import com.tyu.core.model.UserInfoExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service("userInfoApi")
public class UserInfoApi implements IUserInfoApi {

    @Autowired
    private UserInfoMapper userMapper;

    public int registerUser(UserPrincipalVO userPrincipalVO) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userPrincipalVO,userInfo);
        userInfo.setId(IdWorker.getId());
        //salt加密
        String encoderPassword= SHA256HMACEncoder.sha256_HMAC(SHA256Encoder.String2SHA256(userPrincipalVO.getPassword()),userInfo.getUsername());
        userInfo.setPassword(encoderPassword);
        userInfo.setCreateTime(new Date());
        userInfo.setActiveStatus(Constant.ActivityStatus.IS_ACTIVE);

        int state = userMapper.insertSelective(userInfo);

        return state;
    }

    public String login(UserPrincipalVO userPrincipalVO) {
        // 登录逻辑验证 ~~~~~
        UserInfoExample userInfoExample  =  new UserInfoExample();
        userInfoExample.createCriteria().andUsernameEqualTo(userPrincipalVO.getUsername());
        List<UserInfo> userInfos = userMapper.selectByExample(userInfoExample);
        if (CollectionUtils.isEmpty(userInfos)) {
            throw new NotFoundException();
        }
        UserInfo userInfo = userInfos.get(0);

        //salt加密
        String encoderPassword= SHA256HMACEncoder.sha256_HMAC(SHA256Encoder.String2SHA256(userPrincipalVO.getPassword()),userInfo.getUsername());

        //密码加密比较
        if (!encoderPassword.equals(userInfo.getPassword()))
        {
            throw new BusinessException("密码输入错误!");
        }
        // 根据用户信息创建token, 可以把用户其它信息填充进UserPrincipalVO中,提供了全参的构造方法
        BeanUtils.copyProperties(userInfo,userPrincipalVO);

        String token = JWTUtils.createToken(userInfo.toString(), SignTokenConstant.ACCESS);
        return token;
    }
}
