package com.tyu.web.controller;


import com.tyu.core.bean.Job;
import com.tyu.common.util.IdWorker;
import com.tyu.constants.JobStatus;
import com.tyu.constants.JobTopic;
import com.tyu.web.core.dto.Result;
import com.tyu.web.service.DelayQueueApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * @author crossoverFish
 * @date 2018/1/27 下午9:18
 */
@Api("延迟队列相关接口")
@RestController
public class DelayQueueController {


    @ApiOperation("添加延迟任务")
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public Result push(@ApiParam(name = "topic", value = "任务类型", required = true) @RequestParam("topic") String topic,
                       @ApiParam(name = "delayTime", value = "延迟任务执行时间（13位时间时间戳）", required = true) @RequestParam("delayTime") Long delayTime,
                       @ApiParam(name = "ttrTime", value = "延迟任务执行超时时间（单位：秒）", required = true) @RequestParam("ttrTime") Long ttrTime,
                       @ApiParam(name = "message", value = "消息内容", required = true) @RequestParam("message") String message) {
        Job job = new Job();
        job.setTopic(topic);
        job.setDelayTime(delayTime);
        job.setTtrTime(ttrTime);
        job.setMessage(message);
        job.setId(IdWorker.getId());
        job.setStatus(JobStatus.DELAY);
        DelayQueueApi.push(job);
        return Result.sucess();
    }

    @ApiOperation("添加延迟任务")
    @RequestMapping(value = "/pushTest", method = RequestMethod.POST)
    public Result pushTest() {
        Job job = new Job();
        job.setTopic(JobTopic.TOPIC_ONE);
        job.setDelayTime(System.currentTimeMillis()+10000);
        job.setMessage("crossoverFish");
        job.setTtrTime(10000);
        job.setId(IdWorker.getId());
        job.setStatus(JobStatus.DELAY);
        DelayQueueApi.push(job);
        return Result.sucess();
    }

    @ApiOperation("轮询队列获取任务")
    @RequestMapping(value = "/pop/{topic}", method = RequestMethod.GET)
    public Result pop(@PathVariable("topic") String topic) {
        Job job = DelayQueueApi.pop(topic);
        return Result.sucess().put("data", job);
    }

    @ApiOperation("完成任务")
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public Result finish(@ApiParam(name = "id", value = "任务id", required = true) @RequestParam("id") Long id) {
        DelayQueueApi.finish(id);
        return Result.sucess();
    }

    @ApiOperation("异常测试")
    @RequestMapping(value = "/testEx", method = RequestMethod.GET)
    public Result testEx() {
        int i = 1 / 0;
        return Result.sucess();
    }
}
