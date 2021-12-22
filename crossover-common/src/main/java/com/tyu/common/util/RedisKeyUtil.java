package com.tyu.common.util;

public class RedisKeyUtil
{

    public static final String DELAY_BUCKET_KEY_PREFIX = "delayBucket";
    public static final String DELAY_QUEUE_JOB_POOL = "delayQueueJobPool";
    public static final long  DELAY_BUCKET_NUM = 1L;

    public static String getDelayBucketKey(long delayQueueJobId) {
        return DELAY_BUCKET_KEY_PREFIX+Math.floorMod(delayQueueJobId,DELAY_BUCKET_NUM);
    }


    /**
     * 用户登录状态
     *
     * @param userId 用户Id
     * @return key
     */
    public static String getUserIsLoginKey(String userId) {
        return String.format("USER_LOGIN:%s", userId);
    }


    /**
     * 分布式序列 workerIdKey
     * @return
     */
    public static String getWorkerIdKey() {
        return "WORKER_ID";
    }

    /**
     * 分布式序列 hostWorkerIdKey
     * @return
     */
    public static String getHostWorkerKey(String applicationName) {
        return String.format("HOST_WORKER_ID:%s", applicationName);
    }
}