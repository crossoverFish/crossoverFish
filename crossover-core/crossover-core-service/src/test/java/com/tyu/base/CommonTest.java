package com.tyu.base;

import com.alibaba.fastjson.JSONObject;
import com.github.phantomthief.pool.KeyAffinityExecutor;
import com.tyu.entity.Person;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class CommonTest {
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 亲缘性线程:把相同key的任务按照投递线程池的顺序，放到同一个内存队列(这里我们设置为10大小)，每个内存队列有一个线程来消费。
     */
    @Test
    @SneakyThrows
    public void testKeyAffinityExecutor(){
        Person tyu = Person.builder().personAge(11).personName("tyu").build();
        Person zxf = Person.builder().personAge(12).personName("zxf").build();
        Person yyx = Person.builder().personAge(11).personName("yyx").build();
        Person zqx = Person.builder().personAge(12).personName("zqx").build();
        Person xpy = Person.builder().personAge(11).personName("xpy").build();
        List<Person> personList = Arrays.asList(tyu, zxf, yyx,zqx,xpy);
        KeyAffinityExecutor<Object> keyAffinityExecutor = KeyAffinityExecutor.newSerializingExecutor(personList.size(), 10, "KeyAffinityExecutor");
        personList.forEach(p -> keyAffinityExecutor.executeEx(p.getPersonAge(),() -> {
            TimeUnit.MILLISECONDS.sleep(1000);
            logger.info(JSONObject.toJSONString(p));
        }));
        Thread.currentThread().join();
    }


    /**
     * 队列数量太大会阻塞在队列里,创建线程的速度会变慢
     */
    @Test
    @SneakyThrows
    public void testActiveThread() {
        BlockingQueue queue = new LinkedBlockingQueue(100);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 500, 0, TimeUnit.SECONDS, queue);
        CountDownLatch countDownLatch = new CountDownLatch(100);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 100; i++) {
            int finalI1 = i;
            executor.execute(() -> {
                try {
                    System.out.println(String.format("当前线程:%s",finalI1));
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println(String.format("活跃线程:%s",executor.getActiveCount()));
                    System.out.println(String.format("队列数量:%s",executor.getQueue().size()));
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
        countDownLatch.await();
        stopWatch.stop();
        System.out.println(String.format("执行时间:%s",stopWatch.getTime(TimeUnit.SECONDS)));
    }


}
