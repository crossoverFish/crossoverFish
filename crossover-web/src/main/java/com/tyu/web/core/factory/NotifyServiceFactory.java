package com.tyu.web.core.factory;

import com.tyu.common.enums.NotifyEnum;
import com.tyu.web.service.INotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class NotifyServiceFactory {


    @Autowired
    public Map<String, INotifyService> notifyServiceMap;

    /**
     * 获取通知对象
     * @param notifyEnum
     * @return
     */
    public INotifyService getNotifyService(NotifyEnum notifyEnum) {
        INotifyService notifyService = notifyServiceMap.get(notifyEnum.getClazz());
        return notifyService;
    }


}
