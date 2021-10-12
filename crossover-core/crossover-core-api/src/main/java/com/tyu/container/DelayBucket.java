package com.tyu.container;

import com.alibaba.fastjson.JSONObject;
import com.tyu.common.util.RedisUtil;
import com.tyu.core.bean.DelayJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * 一组以时间为维度的有序队列，用来存放所有需要延迟的DelayJob（这里只存放DelayJob Id） zset
 * @author crossoverFish
 * @date 2018/1/27 上午12:41
 */
public class DelayBucket {

    private static final Logger logger = LoggerFactory.getLogger(DelayBucket.class);

    /**
     * 添加 DelayJob 到 延迟任务桶中 zset
     * @param key
     * @param delayJob
     */
    public static void addToBucket(String key, DelayJob delayJob) {
        RedisUtil.ZSetOps.zAdd(key,JSONObject.toJSONString(delayJob), delayJob.getDelayTime());
    }

    /**
     * 从延迟任务桶中获取延迟时间最小的 jobId
     * @param key
     * @return
     */
    public static DelayJob getFromBucket(String key) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = RedisUtil.ZSetOps.zRangeWithScores(key,0,1);
        if (typedTuples.size() == 0) {
            return null;
        }
        ZSetOperations.TypedTuple typedTuple = (ZSetOperations.TypedTuple) typedTuples.toArray()[0];
        return JSONObject.parseObject(String.valueOf(typedTuple.getValue()), DelayJob.class);
    }

    /**
     * 从延迟任务桶中删除 jobId
     * @param key
     * @param delayJob
     */
    public static void deleteFormBucket(String key, DelayJob delayJob) {
        RedisUtil.ZSetOps.zRemove(key,JSONObject.toJSONString(delayJob));
    }
}
