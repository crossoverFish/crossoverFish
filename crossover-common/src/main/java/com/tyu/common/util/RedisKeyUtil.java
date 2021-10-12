package com.tyu.common.util;

public class RedisKeyUtil
{

    public static final String DELAY_BUCKET_KEY_PREFIX = "delayBucket";
    public static final String DELAY_QUEUE_JOB_POOL = "delayQueueJobPool";
    public static final long  DELAY_BUCKET_NUM = 1L;

    public static String getDelayBucketKey(long delayQueueJobId) {
        return DELAY_BUCKET_KEY_PREFIX+Math.floorMod(delayQueueJobId,DELAY_BUCKET_NUM);
    }



}