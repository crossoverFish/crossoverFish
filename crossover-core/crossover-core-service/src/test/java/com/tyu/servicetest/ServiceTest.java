package com.tyu.servicetest;


import com.tyu.application.BaseTestApplication;
import com.tyu.common.util.RedisUtilExpired;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class ServiceTest extends BaseTestApplication {

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testRedis() {
        RedisUtilExpired.StringOps.set("a","b");
    }
}
