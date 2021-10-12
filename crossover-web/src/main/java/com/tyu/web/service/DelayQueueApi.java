package com.tyu.web.service;

import com.tyu.common.util.RedisKeyUtil;
import com.tyu.constants.JobStatus;
import com.tyu.container.DelayBucket;
import com.tyu.container.JobPool;
import com.tyu.container.ReadyQueue;
import com.tyu.core.bean.DelayJob;
import com.tyu.core.bean.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tyu.common.util.RedisKeyUtil.DELAY_BUCKET_NUM;


/**
 * 延迟消息队列
 * @author crossoverFish
 * @date 2018/1/27 上午11:28
 */
public class DelayQueueApi {

    private static final Logger logger = LoggerFactory.getLogger(DelayQueueApi.class);
    public static final String DELAY_BUCKET_KEY_PREFIX = "delayBucket";

    /**
     * 获取delayBucket key 分开多个，有利于提高效率
     * @param delayQueueJobId
     * @return
     */
    private static String getDelayBucketKey(long delayQueueJobId) {
        return DELAY_BUCKET_KEY_PREFIX+Math.floorMod(delayQueueJobId,DELAY_BUCKET_NUM);
    }

    /**
     * 添加延迟任务到延迟队列&有序队列
     * @param job
     */
    public static void push(Job job) {
        JobPool.addDelayQueueJob(job);
        DelayJob item = new DelayJob(job.getId(), job.getDelayTime(),job.getTopic());
        DelayBucket.addToBucket(RedisKeyUtil.getDelayBucketKey(job.getId()),item);
    }

    /**
     * 获取准备好的延迟任务
     * @param topic
     * @return
     */
    public static Job pop(String topic) {
        Long delayQueueJobId = ReadyQueue.pollFormReadyQueue(topic);
        if (delayQueueJobId == null) {
            return null;
        } else {
            Job delayQueueJob = JobPool.getDelayQueueJob(delayQueueJobId);
            if (delayQueueJob == null) {
                return null;
            } else {
                //获取消费超时时间，设置状态为消费待完成状态,重新放到延迟任务桶中
                delayQueueJob.setStatus(JobStatus.RESERVED);
                long reDelayTime = System.currentTimeMillis()+delayQueueJob.getTtrTime();
                delayQueueJob.setDelayTime(reDelayTime);
                JobPool.addDelayQueueJob(delayQueueJob);

                DelayJob item = new DelayJob(delayQueueJob.getId(), reDelayTime,topic);
                DelayBucket.addToBucket(getDelayBucketKey(delayQueueJob.getId()),item);
                //返回的时候设置回
                return delayQueueJob;
            }
        }
    }

    /**
     * 删除延迟队列任务
     * @param delayQueueJobId
     */
    public static void delete(long delayQueueJobId) {
        finish(delayQueueJobId);
    }

    /**
     * 删除延迟任务池,有序队列bucket,消费队列中的元素
     * @param delayQueueJobId
     */
    public static void finish(long delayQueueJobId) {
        Job delayQueueJob = JobPool.getDelayQueueJob(delayQueueJobId);
        if (delayQueueJob == null) {
            return;
        }
        JobPool.deleteDelayQueueJob(delayQueueJobId);
        DelayJob item = new DelayJob(delayQueueJob.getId(), delayQueueJob.getDelayTime(),delayQueueJob.getTopic());
        //如果在超时时间之前执行完成,删除有序集合中的数据,不再搬运
        DelayBucket.deleteFormBucket(getDelayBucketKey(delayQueueJob.getId()),item);
        //如果超时执行,中间队列可能因为ttr机制重新生成一条记录,虽然前面取出了,这边还需要再删一次,不然会产生重复操作
        ReadyQueue.removeFormReadyQueue(delayQueueJob.getTopic(),delayQueueJobId);
    }

    /**
     * 查询delay job
     * @param delayQueueJobId
     * @return
     */
    public static Job get(long delayQueueJobId) {
        return JobPool.getDelayQueueJob(delayQueueJobId);
    }
}
