package com.tyu.common.util;


import com.tyu.common.constant.SignTokenConstant;
import com.tyu.common.exception.BusinessException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RequestSignUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestSignUtils.class);

    private RequestSignUtils() {
        throw new IllegalStateException("Utility class");
    }




    /**
     * 模拟客户端操作
     *
     * @param args
     */
    public static void main(String[] args) {
        String accessKeyId = SignTokenConstant.ACCESS;
        String accessKeySecret = SignTokenConstant.PARTNERS.get(accessKeyId);
        // 1. 组装请求参数
        Map<String, String> data = new HashMap<>();
        data.put("serialNumber", "17625416661");

        // 2. FORM表单格式参数签名
        String sign = sign(accessKeyId, accessKeySecret, data);



    }

    /**
     * 验证签名 5分钟过期
     *
     * @param accessKeyId
     * @param sign
     * @return
     */
    public static Boolean validate(String accessKeyId, String sign, String timestamp, Map<String, String> paramMap) {
        if (timestamp == null || Math.abs(System.currentTimeMillis() - Long.valueOf(timestamp)) > 5 * 60 * 1000) {
            throw new BusinessException("接口请求超时");
        }
        String secret = validate(accessKeyId, sign);
        Map<String, String> data = new HashMap<>();
        if (null != paramMap) {
            data.putAll(paramMap);
        }
        // 进行sign验证
        if (!sign.equals(sign(accessKeyId, secret, data))) {
            logger.error("sign签名不匹配");
            throw new BusinessException( "sign签名不匹配");
        }
        return true;
    }


    /**
     * 验证签名前准备 - 成功后返回secret
     *
     * @param accessKeyId
     * @param sign
     * @return
     */
    private static String validate(String accessKeyId, String sign) {
        if (StringUtils.isBlank(accessKeyId)) {
            throw new BusinessException( "accessKeyId不存在");
        }
        if (StringUtils.isBlank(sign)) {
            throw new BusinessException("sign不存在");
        }
        String secret = SignTokenConstant.PARTNERS.get(accessKeyId);
        if (StringUtils.isBlank(secret)) {
            logger.error("验证失败, secret不存在");
            throw new BusinessException("secret不存在");
        }
        return secret;
    }

    /**
     * 参数签名
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param data
     * @return
     */
    public static String sign(String accessKeyId, String accessKeySecret, Map<String, String> data) {
        logger.info("参数签名:accessKeyId:{},accessKeySecret:{},data:{}",accessKeyId,accessKeySecret, data);
        signValidate(accessKeyId, accessKeySecret);
        String formatUrlMap = formatUrlMap(data);
        logger.info("formatUrlMap:{}",formatUrlMap);
        String formatUrlMapConcat = formatUrlMap.concat(accessKeySecret);
        logger.info("formatUrlMapConcat:{}",formatUrlMapConcat);
        String sign = md5(md5(formatUrlMapConcat));
        logger.info("sign:{}",sign);
        return sign;
    }


    /**
     * 签名参数验证
     *
     * @param accessKeyId
     * @param accessKeySecret
     */
    private static void signValidate(String accessKeyId, String accessKeySecret) {
        if (isBlank(accessKeyId) || isBlank(accessKeySecret)) {
            throw new NullPointerException("参数签名错误, 至少应该包含accessKeyId和accessKeySecret");
        }
    }

    /**
     * 方法用途: 对所有传入参数按照字段名的ASCII码从小到大排序（字典序），并且生成url参数串
     *
     * @param paraMap 要排序的Map对象
     * @return
     */
    private static String formatUrlMap(Map<String, String> paraMap) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        List<Map.Entry<String, String>> infoIds = new ArrayList<>(tmpMap.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        infoIds.sort(
                (Map.Entry<String, String> o1, Map.Entry<String, String> o2) -> o1.getKey().compareTo(o2.getKey()));
        // 构造URL 键值对的格式
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> item : infoIds) {
            if (!isBlank(item.getKey())) {
                String val = item.getValue();
                try {
                    val = URLEncoder.encode(val, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e.getMessage());
                }
                // 将Key转换为全小写
                buf.append(item.getKey().toLowerCase() + "=" + val.toLowerCase());
                buf.append("&");
            }
        }
        buff = buf.toString();
        if (!buff.isEmpty()) {
            buff = buff.substring(0, buff.length() - 1);
        }
        return buff;
    }



    /**
     * 重载org.apache.commons.lang3的isBlank方法,避免导入包
     *
     * <p>
     * Checks if a CharSequence is empty (""), null or whitespace only.
     * </p>
     *
     * <p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * </p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace only
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    private static String md5(String str) {
        String md5Str = DigestUtils.md5Hex( str);
        return md5Str;
    }


}
