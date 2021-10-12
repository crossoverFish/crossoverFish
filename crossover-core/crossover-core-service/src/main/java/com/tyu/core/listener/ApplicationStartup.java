package com.tyu.core.listener;

import com.tyu.core.handler.DelayHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.tyu.common.util.RedisKeyUtil.DELAY_BUCKET_KEY_PREFIX;
import static com.tyu.common.util.RedisKeyUtil.DELAY_BUCKET_NUM;


/**
 * @author crossoverFish
 * @date 2018/1/27 下午10:15
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ExecutorService executorService = Executors.newFixedThreadPool((int) DELAY_BUCKET_NUM);
        for (int i = 0; i < DELAY_BUCKET_NUM; i++) {
            executorService.execute(new DelayHandler(DELAY_BUCKET_KEY_PREFIX+i));
        }
    }
}
