package com.tyu.container;


import com.tyu.common.util.RedisUtilExpired;

/**
 * 存放可以消费的job list
 * @date 2018/1/27 上午2:04
 */
public class ReadyQueue {

    /**
     * 添加jodid到准备队列
     * @param topic
     * @param delayQueueJobId
     */
    public static void pushToReadyQueue(String topic,long delayQueueJobId) {
        RedisUtilExpired.ListOps.lLeftPush(topic,delayQueueJobId + "");
    }

    /**
     * 从准备队列中获取jodid
     * @param topic
     * @return
     */
    public static Long pollFormReadyQueue(String topic) {
        String jobId = RedisUtilExpired.ListOps.lRightPop(topic);
        return Long.valueOf(jobId);
    }

    /**
     * 从准备队列中删除jobid
     * @param topic
     * @return
     */
    public static void removeFormReadyQueue(String topic,long delayQueueJobId) {
        RedisUtilExpired.ListOps.lRemove(topic,1,delayQueueJobId + "");
    }
}
