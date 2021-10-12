package com.tyu.core.bean;


import com.tyu.constants.JobStatus;
import lombok.Data;


/**
 * 延迟任务
 * @author crossoverFish
 * @date 2018/1/27 上午12:18
 */
@Data
public class Job {

    /**
     * 延迟任务的唯一标识，用于检索任务
     */
    private Long id;

    /**
     * 任务类型（具体业务类型）
     */
    private String topic;

    /**
     * 任务的执行时间
     */
    private long delayTime;

    /**
     * 任务的执行超时时间
     */
    private long ttrTime;

    /**
     * 任务具体的消息内容，用于处理具体业务逻辑用
     */
    private String message;

    /**
     * 任务状态
     */
    private JobStatus status;


    /**
     * ttr延迟时间(不设置默认10s)
     */
    private long ttrDelayTime = 10000;


    /**
     * 重试次数
     */
    private int retryCount = 0;

}
