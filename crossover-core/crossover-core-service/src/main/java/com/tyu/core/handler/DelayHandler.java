package com.tyu.core.handler;


import com.alibaba.fastjson.JSON;
import com.tyu.constants.JobStatus;
import com.tyu.core.bean.DelayJob;
import com.tyu.core.bean.Job;
import com.tyu.container.DelayBucket;
import com.tyu.container.JobPool;
import com.tyu.container.ReadyQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.tyu.constants.DelayConfig.RETRY_COUNT;
import static com.tyu.constants.DelayConfig.SLEEP_TIME;


/**
 * 扫描延迟任务桶中的任务，放到准备队列中
 * @author crossoverFish
 * @date 2018/1/27 下午8:03
 */
public class DelayHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DelayHandler.class);

    private String delayBucketKey;

    public DelayHandler(String delayBucketKey) {
        this.delayBucketKey = delayBucketKey;
    }



    @Override
    public void run() {
        while (true) {
            try {
                DelayJob item = DelayBucket.getFromBucket(this.delayBucketKey);
                //没有任务
                if (item == null) {
                    sleep();
                     continue;
                }
                //延迟时间没到
                if (item.getDelayTime() > System.currentTimeMillis()) {
                    sleep();
                    continue;
                }

                Job delayQueueJob = JobPool.getDelayQueueJob(item.getDelayQueueJobId());
                //延迟任务元数据不存在
                if (delayQueueJob == null) {
                    DelayBucket.deleteFormBucket(this.delayBucketKey,item);
                    continue;
                }

                JobStatus status = delayQueueJob.getStatus();

                if (JobStatus.RESERVED.equals(status)) {
                    logger.info("处理超时任务:{}", JSON.toJSONString(delayQueueJob));
                    // 超时任务
                    processTtrJob(item,delayQueueJob);
                } else {
                    logger.info("处理延时任务:{}", JSON.toJSONString(delayQueueJob));
                    // 延时任务
                    processDelayJob(item,delayQueueJob);
                }

            }catch (Exception e) {
                logger.error("扫描delaybucket出错：",e);
            }


        }
    }

    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(SLEEP_TIME);
        }catch (InterruptedException e){
            logger.error("",e);
        }
    }

    /**
     * 处理ttr的任务,更改job状态为不可执行状态,执行重试操作
     */
    private void processTtrJob(DelayJob delayJob, Job job) {
        //如果重试次数大于指定次数,则搁置,不再处理
        int retryCount = job.getRetryCount();
        if (retryCount > RETRY_COUNT - 1) {
            job.setStatus(JobStatus.PUTASIDE);
            JobPool.addDelayQueueJob(job);
            DelayBucket.deleteFormBucket(this.delayBucketKey,delayJob);
            return;
        }

        // 修改任务池状态
        job.setRetryCount(++retryCount);
        processDelayJob(delayJob,job);
    }


    /**
     * 处理延时任务,更改job状态为可执行状态,将任务从bucket移到list,删除bucket延迟记录
     */
    private void processDelayJob(DelayJob delayJob,Job job) {
        job.setStatus(JobStatus.READY);
        // 修改任务池状态
        JobPool.addDelayQueueJob(job);
        // 设置到待处理任务
        ReadyQueue.pushToReadyQueue(delayJob.getTopic(),delayJob.getDelayQueueJobId());
        // 移除delayBucket中的任务
        DelayBucket.deleteFormBucket(this.delayBucketKey,delayJob);
    }
}
