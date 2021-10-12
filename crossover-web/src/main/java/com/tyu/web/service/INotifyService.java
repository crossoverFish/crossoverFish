package com.tyu.web.service;


import java.util.Map;

public interface INotifyService {

    /**
     * 发送异步消息前置处理
     * @param param
     * @return
     */
    Object beforeSendNotice(Map<String, Object> param);

    /**
     * 异步通知真正实现接口
     * @param param 入参
     */
    void dealNotice(Map<String, Object> param);
}
