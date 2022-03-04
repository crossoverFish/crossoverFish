package com.tyu.common.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tyu.common.constant.SignTokenConstant;
import com.tyu.common.exception.BusinessException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.constant.ErrorConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;


@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtils {


    //有效期 默认30分钟
    public static long expireTime = 30*60*1000;



    /**
     * 创建TOKEN
     * @param userInfo
     * @return
     */
    @SneakyThrows
    public static String createToken(String userInfo,String accessKey){
        String accessKeySecret = SignTokenConstant.PARTNERS.get(accessKey);
        String encryptClaims = AesEncryptUtil.encrypt(userInfo, accessKeySecret, accessKeySecret);
        return SignTokenConstant.TOKEN_PREFIX + JWT.create()
                .withSubject(encryptClaims)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC512(accessKeySecret));
    }

    public static void main(String[] args) {
        String userInfo = "{\"id\":111111,\"password\":\"abd!2123\",\"username\":\"撒大大\"}";
        String accessKeyId = SignTokenConstant.ACCESS;
        String token = createToken(userInfo, accessKeyId);
        System.out.println(token);
        String decryptUserInfo = validateToken(token, accessKeyId);
        System.out.println(decryptUserInfo);
    }


    /**
     * 验证token
     * @param token
     */
    public static String validateToken(String token,String accessKey){
        try {
            String accessKeySecret = SignTokenConstant.PARTNERS.get(accessKey);
            String encryptedStr = JWT.require(Algorithm.HMAC512(accessKeySecret))
                    .build()
                    .verify(token.replace(SignTokenConstant.TOKEN_PREFIX, ""))
                    .getSubject();
            String decryptedStr = AesEncryptUtil.decrypt(encryptedStr, accessKeySecret, accessKeySecret);
            return decryptedStr;
        } catch (TokenExpiredException e){
            throw new BusinessException("token已经过期");
        } catch (Exception e){
            throw new BusinessException("token验证失败");
        }
    }

    /**
     * 检查token是否需要更新
     * @param token
     * @return
     */
    public static boolean isNeedUpdate(String token,String accessKey){
        //获取token过期时间
        Date expiresAt = null;
        try {
            String secret = SignTokenConstant.PARTNERS.get(accessKey);
            expiresAt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(SignTokenConstant.TOKEN_PREFIX, ""))
                    .getExpiresAt();
        } catch (TokenExpiredException e){
            return true;
        } catch (Exception e){
            throw new BusinessException("token验证失败");
        }
        //如果剩余过期时间少于过期时常的一般时 需要更新
        return (expiresAt.getTime()-System.currentTimeMillis()) < (expireTime>>1);
    }



}


