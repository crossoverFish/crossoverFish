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
import org.apache.poi.ss.formula.functions.T;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

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

    public void init(){
        userMapper.deleteByPrimaryKey(1L);
        UserInfo userInfo = UserInfo.builder().id(1L).username("zxf").password("1").activeStatus("1").createTime(new Date()).build();
        userMapper.insert(userInfo);
      return;
    }

    @Override
    public void testTransaction() {
        init();
        UserInfoApi userInfoApi = (UserInfoApi) AopContext.currentProxy();
        userInfoApi.sql1();
    }


    //1.捕获不到异常都会执行,反之都不会执行
    @Transactional(rollbackFor = Exception.class)
    public void sql1() {
        UserInfo userInfo = UserInfo.builder().id(1L).password("tyu").build();
        userMapper.updateByPrimaryKeySelective(userInfo);
        IUserInfoApi userInfoApi = (UserInfoApi) AopContext.currentProxy();
        userInfoApi.sq2();
        System.out.println(1/0);

    }
    @Transactional(rollbackFor = Exception.class,propagation = REQUIRES_NEW)
    @Override
    public void sq2() {
            UserInfo userInfo = UserInfo.builder().id(1L).username("xxx").build();
            userMapper.updateByPrimaryKeySelective(userInfo);
    }
}
