package com.tyu.web.service.impl;


import com.tyu.web.service.INotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
public class CutPriceSuccessNotifyServiceImpl extends AbstractNotifyService implements INotifyService {
    private Logger logger = LoggerFactory.getLogger(CutPriceSuccessNotifyServiceImpl.class);


    /**
     * 异步通知前置参数处理方法
     * 主要目的是准备要发送的参数以及要给哪些人发送
     * @param param
     * @return
     */
    @Override
    public Object beforeSendNotice(Map<String, Object> param) {
        logger.info("开始进行处理砍价成功异步通知 前置处理");
        return null;
    }
}
