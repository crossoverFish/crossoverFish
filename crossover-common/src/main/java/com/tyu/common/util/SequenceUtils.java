package com.tyu.common.util;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.tyu.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * 雪花漂移算法主键ID生成工具
 * 在默认配置下，ID可用 71000 年不重复。
 * 在支持 1024 个工作节点时，ID可用 4480 年不重复。
 * 在支持 4096 个工作节点时，ID可用 1120 年不重复
 */
public class SequenceUtils {


    @Value("${spring.application.name}")
    private String application;



    /**
     * 并非所有实现都支持跨进程的并发唯一,保险起见,如果一台服务器部署多个独立服务，需要为每个服务指定不同的 WorkerId。
     * WorkerIdBitLength，机器码位长,决定 WorkerId 的最大值，这里WorkerIdBitLength暂设置成7,可以支持 2^7-1即127个节点服务使用,规则要求：WorkerIdBitLength + SeqBitLength 不超过 22。
     * SeqBitLength，序列数位长，决定每毫秒基础生成的ID个数，默认值6，取值范围 [3, 21]（建议不小于4）。规则要求：WorkerIdBitLength + SeqBitLength 不超过 22。
     * 任何时候增加 WorkerIdBitLength 或 SeqBitLength，都是可以的，但是慎用 “减小”的操作，因为这可能导致在未来某天生成的 ID 与过去老配置时相同。
     * 如果必须减小 WorkerIdBitLength 或 SeqBitLength 其中的一项，一定要满足一个条件：新的两个 BitLength 之和要大于 老的值之和。[不推荐在运行之后缩小任何一个 BitLength 值]
     */
    @PostConstruct
    public void init() throws Exception {
        Long workerId = 0L;
        String[] activeProfiles = SpringContextHolderUtil.activeProfiles;
        if (Arrays.asList(activeProfiles).contains("prod")){
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String hostWorkerKey = RedisKeyUtil.getHostWorkerKey(application);
            if (RedisUtil.HashOps.hashKey(hostWorkerKey, hostAddress)) {
                workerId = RedisUtil.HashOps.increment(hostWorkerKey, hostAddress, 0);
            } else {
                workerId = RedisUtil.StringOps.increment(RedisKeyUtil.getWorkerIdKey(), 1);
                RedisUtil.HashOps.hPut(hostWorkerKey, hostAddress, workerId.intValue());
            }
        }else {
            workerId = 1L;
        }
        IdGeneratorOptions options = new IdGeneratorOptions(workerId.shortValue());
        //这个值越大生成的序列越长,支撑的workerId也越多
        options.WorkerIdBitLength = 7;
        options.SeqBitLength = 6;
        if (options.WorkerIdBitLength + options.SeqBitLength > 22) {
            throw new BusinessException("规则要求：WorkerIdBitLength + SeqBitLength 不超过 22");
        }
        if (workerId > Math.pow(2, options.WorkerIdBitLength) - 1) {
            throw new BusinessException("规则要求：WorkerId < 2^WorkerIdBitLength-1");
        }

        YitIdHelper.setIdGenerator(options);

    }


    public static Long getSequenceId() {
        return YitIdHelper.nextId();
    }

}
