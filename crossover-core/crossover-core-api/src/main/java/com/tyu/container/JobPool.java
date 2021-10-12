package com.tyu.container;

import com.alibaba.fastjson.JSONObject;
import com.tyu.common.util.RedisUtil;
import com.tyu.core.bean.Job;

import static com.tyu.common.util.RedisKeyUtil.DELAY_QUEUE_JOB_POOL;


/**
 * 延迟任务池 hash
 * @date 2018/1/27 上午1:35
 */
public class JobPool {


    /**
     * 查询 DelayQueueJob
     * @param delayQueueJobId
     * @return
     */
    public static Job getDelayQueueJob(long delayQueueJobId) {
        String delayQueueJob = (String) RedisUtil.HashOps.hGet(DELAY_QUEUE_JOB_POOL, delayQueueJobId + "");
        return JSONObject.parseObject(delayQueueJob, Job.class);
    }

    /**
     * 添加 任务元信息到redis(hash)
     * @param job
     */
    public static void addDelayQueueJob(Job job) {
        RedisUtil.HashOps.hPut(DELAY_QUEUE_JOB_POOL,String.valueOf(job.getId()), JSONObject.toJSONString(job));
    }

    /**
     * 删除 DelayQueueJob
     * @param delayQueueJobId
     */
    public static void deleteDelayQueueJob(long delayQueueJobId) {
        RedisUtil.HashOps.hDelete(DELAY_QUEUE_JOB_POOL,delayQueueJobId+"");
    }
}
