package com.tyu.common.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tyu.common.constant.SignTokenConstant;
import com.tyu.common.exception.BusinessException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtils {


    //有效期 默认30分钟
    public static long expireTime = 30*60*1000;



    /**
     * 创建TOKEN
     * @param sub
     * @return
     */
    public static String createToken(String sub,String accessKey){
        String secret = SignTokenConstant.PARTNERS.get(accessKey);
        return SignTokenConstant.TOKEN_PREFIX + JWT.create()
                .withSubject(sub)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC512(secret));
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getDateTime((new Date(System.currentTimeMillis() + expireTime))));
    }


    /**
     * 验证token
     * @param token
     */
    public static String validateToken(String token,String accessKey){
        try {
            String secret = SignTokenConstant.PARTNERS.get(accessKey);
            return JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(SignTokenConstant.TOKEN_PREFIX, ""))
                    .getSubject();
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


