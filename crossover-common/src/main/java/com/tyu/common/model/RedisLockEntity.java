package com.tyu.common.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

@Data
@RequiredArgsConstructor(staticName = "of")
@Accessors(chain = true)
public class RedisLockEntity {
    private String lockKey;
    private String requestId;
    private Long lockTime;
    private TimeUnit timeUnit;
    private Long retryTimeoutLimit;

}
