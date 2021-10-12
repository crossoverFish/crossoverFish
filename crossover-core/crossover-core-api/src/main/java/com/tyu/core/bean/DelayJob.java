package com.tyu.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crossoverFish
 * @date 2018/1/27 上午1:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelayJob {


    /**
     * 延迟任务的唯一标识
     */
    private long delayQueueJobId;

    /**
     * 任务的执行时间
     */
    private long delayTime;

    /**
     * 任务类型（具体业务类型）
     */
    private String topic;


}
