package com.tyu.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum NotifyEnum {

    CUTPRICE_SUCCESS("cutPrice","砍价成功异步通知准备参数方法","cutPriceSuccessNotifyServiceImpl"),
    SECKILL_SUCCESS("secKill","秒杀成功异步通知准备参数方法","secKillSuccessNotifyServiceImpl"),
    GROUP_SUCCESS("group","拼团成功异步通知准备参数方法","groupSuccessNotifyServiceImpl");

    public static final Map<String,NotifyEnum> notifyMap ;

    static {
        Map<String,NotifyEnum> tempMap  = new HashMap();
        tempMap.put("cutPrice",CUTPRICE_SUCCESS);
        tempMap.put("secKill",SECKILL_SUCCESS);
        tempMap.put("group",GROUP_SUCCESS);
        notifyMap = Collections.unmodifiableMap(tempMap);
    }


    /** 枚举值码 */
    private final String commandType;

    /** 枚举描述 */
    private final String desc;

    /**
     * 实现类
     */
    private final String clazz ;

    /**
     * 得到枚举值码。
     * @return 枚举值码。
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * 得到枚举描述。
     * @return 枚举描述。
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 获取 class。
     * @return class。
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * 构建一个 。
     * @param commandType 枚举值码。
     * @param desc 枚举描述。
     */
     NotifyEnum(String commandType, String desc, String clazz) {
        this.commandType = commandType;
        this.desc = desc;
        this.clazz = clazz ;
    }


    public static NotifyEnum buildFromNotifyType(String notifyType) {
        if (StringUtils.isEmpty(notifyType)) {
            return null;
        }
        return notifyMap.get(notifyType);
    }
}
