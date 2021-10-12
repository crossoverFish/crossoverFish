package com.tyu.web.service.impl;


import com.tyu.web.service.INotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractNotifyService implements INotifyService {
    private Logger logger = LoggerFactory.getLogger(AbstractNotifyService.class);


    /**
     * 异步通知前置参数处理方法
     * @param param
     * @return
     */
    @Override
    public abstract Object beforeSendNotice(Map<String, Object> param);

    /**
     * 异步通知真正实现接口
     * @param param 入参
     */
    @Override
    public void dealNotice(Map<String, Object> param) {
        logger.info("真正开始进行异步通知处理");
        //装载数据
        Object message = beforeSendNotice(param);
        //处理message
    }
}
