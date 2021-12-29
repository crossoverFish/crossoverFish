package com.tyu.web.core.handler;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.tyu.common.anno.AccessToken;
import com.tyu.common.anno.Sign;
import com.tyu.common.constant.SignTokenConstant;
import com.tyu.common.util.*;
import com.tyu.core.model.UserPrincipalVO;
import org.apache.catalina.connector.OutputBuffer;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求拦截器
 *
 * @author crossoverFish
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime.set(System.currentTimeMillis());
        //放行非继承自HandlerMethod的处理器
        if (!(handler instanceof  HandlerMethod)) {return true;}
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        Boolean signIsCorrect = true;
        Map paramMap = printRequest(request);
        logger.info("进入请求 ==> uri:{} : 入参:{}",request.getServletPath(), paramMap);

        Sign sign = method.getAnnotation(Sign.class);
        if (sign != null && sign.hasSign()) {
            signIsCorrect = checkSign(request,paramMap);
        }

        Boolean tokenIsCorrect = true;
        AccessToken token = method.getAnnotation(AccessToken.class);
        if (token != null && token.hasToken()) {
            tokenIsCorrect = tokenAuthentication(request, response);
        }
        return signIsCorrect&&tokenIsCorrect;
    }

    private Boolean checkSign(HttpServletRequest request, Map paramMap) throws IOException {

        Boolean signIsCorrect;
        String accessKeyId = request.getHeader(SignTokenConstant.ACCESS_KEY_ID);
        String headerSign = request.getHeader(SignTokenConstant.HEADER_SIGN);
        String headerTime = request.getHeader(SignTokenConstant.HEADER_TIME);
        //获取请求body
        byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
        if (bodyBytes.length > 0) {
            String body = new String(bodyBytes, request.getCharacterEncoding());
             paramMap = JSONObject.parseObject(body, new TypeReference<Map<String, String>>(){});
        } else {
            String paraName;
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                paraName = parameterNames.nextElement();
                paramMap.put(paraName, request.getParameter(paraName));
            }
        }
        signIsCorrect = RequestSignUtils.validate(accessKeyId,
                headerSign, headerTime, paramMap);
        return signIsCorrect;
    }




    private boolean tokenAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(SignTokenConstant.USER_LOGIN_TOKEN);
        //验证token
        String userInfo = JWTUtils.validateToken(token,SignTokenConstant.ACCESS);
        if (StringUtils.isBlank(userInfo)) {
            logger.warn("token校验结果为空!");
            return false;
        }
        UserPrincipalVO userPrincipalVO = JSONObject.parseObject(userInfo, UserPrincipalVO.class);
        request.setAttribute("userInfo", userPrincipalVO);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e)
            throws Exception {
        printResponse(request,response);
        startTime.remove();
    }

    public Map printRequest(HttpServletRequest request) throws Exception {
        //获取请求body
        Map  paramMap = Maps.newHashMap();
        byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
        if (bodyBytes.length > 0) {
            String body = new String(bodyBytes, request.getCharacterEncoding());
            paramMap = JacksonCamelUtil.objectToMap(JSONObject.parseObject(body));
        } else {
            String paraName;
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                paraName = parameterNames.nextElement();
                paramMap.put(paraName, request.getParameter(paraName));
            }
        }

        return paramMap;
    }

    public void printResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResponseFacade responseFacade = (ResponseFacade) response;
        ServletOutputStream servletOutputStream = responseFacade.getOutputStream();
        Field field = servletOutputStream.getClass().getDeclaredField("ob");
        field.setAccessible(true);
        OutputBuffer outputBuffer = (OutputBuffer) field.get(servletOutputStream);
        Field field1 = outputBuffer.getClass().getDeclaredField("bb");
        field1.setAccessible(true);
        ByteBuffer byteBuffer = (ByteBuffer) field1.get(outputBuffer);
        byte[] bytes = byteBuffer.array();
        long times = System.currentTimeMillis() - startTime.get();
        logger.info("退出请求 ==> uri:{} 耗时:{}ms 响应内容:{}", request.getServletPath(), times ,new String(bytes, "UTF-8"));

    }

}

