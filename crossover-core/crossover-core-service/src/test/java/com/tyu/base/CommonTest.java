package com.tyu.base;

import com.alibaba.fastjson.JSONObject;
import com.github.phantomthief.pool.KeyAffinityExecutor;
import com.tyu.entity.Person;
import com.tyu.service.IDrink;
import com.tyu.service.IEat;
import com.tyu.service.impl.UserExtend;
import com.tyu.service.impl.UserService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
        System.out.println(String.format("执行时间:%s",stopWatch.getTime()));
    }


    @Test
    public void  testFlatMap() {
        String[] strings = new String[]{"hello","world"};
        System.out.println(Arrays.stream(strings).map(str -> str.split("")).flatMap(Arrays::stream).distinct().sorted().collect(Collectors.toList()));
    }


    /**
     * 静态代理一个代理类只能代理一个接口.如果一个类实现了多个接口,想要都被代理,只能创建多个代理类
     */
    @Test
    public void reflectTest() {
        /**
         * jdk的动态代理是基于接口的动态代理,要求目标对象必须实现至少一个接口,核心API是java.lang.reflect.Proxy类的newProxyInstance方法。
         */
        IEat eat = new UserService();
        IEat myproxy = (IEat) Proxy.newProxyInstance(eat.getClass().getClassLoader(), eat.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object invoke = method.invoke(eat, args);
                return invoke;
            }
        });
        myproxy.eat("apple");

        /**
         * cglib的动态代理是基于子类的动态代理,不需要目标对象实现接口
         */
        UserExtend userExtend = new UserExtend();
        UserExtend aaaaaaaaa = (UserExtend) Enhancer.create(userExtend.getClass(), new MethodInterceptor() {
            /**
             * @param proxy：代理对象引用
             * @param method：目标对象方法（通过它可以访问目标对象）
             * @param args：传递给目标对象的参数
             * @MethodProxy methodProxy：代理对象的方法
             * @return 返回值
             * @throws Throwable
             */
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                //功能代码
                logger.info("aaaaaaaaa");
                Object invoke = method.invoke(userExtend, args);
                return invoke;
            }
        });
        aaaaaaaaa.eat("apple");
    }
}
