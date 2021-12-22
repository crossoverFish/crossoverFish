package com.tyu.common.util;


import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 *
 * @author qxzhou
 * @date 2021-11-23 20:54:48
 */
@Component
@Slf4j
public class RedisUtil {


    private static RedisTemplate redisTemplate;


    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //- - - - - - - - - - - - - - - - - - - - -  公共方法 - - - - - - - - - - - - - - - - - - - -


    public static class KeyOps {
        /**
         * 给一个指定的 key 值附加过期时间
         *
         * @param key
         * @param time
         * @return
         */
        public static boolean expire(String key, long time) {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }


        /**
         * 删除一个指定的 key
         *
         * @param key
         * @return
         */
        public static boolean delete(String key) {
            return redisTemplate.delete(key);
        }

        /**
         * 根据key 获取过期时间
         *
         * @param key
         * @return
         */
        public static long getTime(String key) {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        }

        /**
         * 根据key 获取过期时间
         *
         * @param key
         * @return
         */
        public static boolean hasKey(String key) {
            return redisTemplate.hasKey(key);
        }

        /**
         * 移除指定key 的过期时间
         *
         * @param key
         * @return
         */
        public static boolean persist(String key) {
            return redisTemplate.boundValueOps(key).persist();
        }
    }


    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -
    public static class StringOps {
        /**
         * 根据key获取值
         *
         * @param key 键
         * @return 值
         */
        public static Object get(String key) {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        }

        /**
         * 将值放入缓存
         *
         * @param key   键
         * @param value 值
         * @return true成功 false 失败
         */
        public static <T>void set(String key, T value) {
            redisTemplate.opsForValue().set(key, value);
        }

        /**
         * 将值放入缓存并设置时间
         *
         * @param key   键
         * @param value 值
         * @param time  时间(秒) -1为无期限
         * @return true成功 false 失败
         */
        public static <T>void set(String key, T value, long time) {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
        }

        public static <T> void set(String key, T value, long time, TimeUnit timeUnit) {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
        }

        /**
         * 批量添加 key (重复的键会覆盖)
         *
         * @param keyAndValue
         */
        public static void batchSet(Map keyAndValue) {
            redisTemplate.opsForValue().multiSet(keyAndValue);
        }

        /**
         * 批量添加 key-value 只有在键不存在时,才添加
         * map 中只要有一个key存在,则全部不添加
         *
         * @param keyAndValue
         */
        public static void batchSetIfAbsent(Map keyAndValue) {
            redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
        }

        /**
         * 对一个 key-value 的值进行加减操作,
         * 如果该 key 不存在 将创建一个key 并赋值该 number
         * 如果 key 存在,但 value 不是长整型 ,将报错
         *
         * @param key
         * @param number
         */
        public static Long increment(String key, long number) {
            return redisTemplate.opsForValue().increment(key, number);
        }

        /**
         * 对一个 key-value 的值进行加减操作,
         * 如果该 key 不存在 将创建一个key 并赋值该 number
         * 如果 key 存在,但 value 不是 纯数字 ,将报错
         *
         * @param key
         * @param number
         */
        public static Double increment(String key, double number) {
            return redisTemplate.opsForValue().increment(key, number);
        }
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -
    public static class SetOps {
        /**
         * 将数据放入set缓存
         *
         * @param key 键
         * @return
         */
        public static void sSet(String key, String value) {
            redisTemplate.opsForSet().add(key, value);
        }

        /**
         * 获取变量中的值
         *
         * @param key 键
         * @return
         */
        public static Set members(String key) {
            return redisTemplate.opsForSet().members(key);
        }

        /**
         * 随机获取变量中指定个数的元素
         *
         * @param key   键
         * @param count 值
         * @return
         */
        public static void randomMembers(String key, long count) {
            redisTemplate.opsForSet().randomMembers(key, count);
        }

        /**
         * 随机获取变量中的元素
         *
         * @param key 键
         * @return
         */
        public static Object randomMember(String key) {
            return redisTemplate.opsForSet().randomMember(key);
        }

        /**
         * 弹出变量中的元素
         *
         * @param key 键
         * @return
         */
        public static Object pop(String key) {
            return redisTemplate.opsForSet().pop("setValue");
        }

        /**
         * 获取变量中值的长度
         *
         * @param key 键
         * @return
         */
        public static long size(String key) {
            return redisTemplate.opsForSet().size(key);
        }

        /**
         * 根据value从一个set中查询,是否存在
         *
         * @param key   键
         * @param value 值
         * @return true 存在 false不存在
         */
        public static boolean sHasKey(String key, Object value) {
            return redisTemplate.opsForSet().isMember(key, value);
        }

        /**
         * 检查给定的元素是否在变量中。
         *
         * @param key 键
         * @param obj 元素对象
         * @return
         */
        public static boolean isMember(String key, Object obj) {
            return redisTemplate.opsForSet().isMember(key, obj);
        }

        /**
         * 转移变量的元素值到目的变量。
         *
         * @param key     键
         * @param value   元素对象
         * @param destKey 元素对象
         * @return
         */
        public static boolean move(String key, String value, String destKey) {
            return redisTemplate.opsForSet().move(key, value, destKey);
        }

        /**
         * 批量移除set缓存中元素
         *
         * @param key    键
         * @param values 值
         * @return
         */
        public static void remove(String key, Object... values) {
            redisTemplate.opsForSet().remove(key, values);
        }

        /**
         * 通过给定的key求2个set变量的差值
         *
         * @param key     键
         * @param destKey 键
         * @return
         */
        public static Set difference(String key, String destKey) {
            return redisTemplate.opsForSet().difference(key, destKey);
        }

    }

    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -
    public static class HashOps {
        /**
         * 加入缓存
         *
         * @param key 键
         * @param map 键
         * @return
         */
        public static void add(String key, Map map) {
            redisTemplate.opsForHash().putAll(key, map);
        }

        /**
         * 向key对应的hash中，增加一个键值对entryKey-entryValue
         *
         * @param key
         * @param entryKey
         * @param entryValue
         */
        public static <T> void hPut(String key, String entryKey, T entryValue) {
            redisTemplate.opsForHash().put(key, entryKey, entryValue);
        }


        /**
         * 获取 key 下的 所有  hashkey 和 value
         *
         * @param key 键
         * @return
         */
        public static Map getHashEntries(String key) {
            return redisTemplate.opsForHash().entries(key);
        }

        /**
         * 验证指定 key 下 有没有指定的 hashkey
         *
         * @param key
         * @param hashKey
         * @return
         */
        public static boolean hashKey(String key, String hashKey) {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        }

        /**
         * 获取指定key的值string
         *
         * @param key  键
         * @param key2 键
         * @return
         */
        public static String getMapString(String key, String key2) {
            return redisTemplate.opsForHash().get(key, key2).toString();
        }

        /**
         * 获取指定的值Int
         *
         * @param key  键
         * @param key2 键
         * @return
         */
        public static Integer getMapInt(String key, String key2) {
            return (Integer) redisTemplate.opsForHash().get("map1", "key1");
        }

        /**
         * 弹出元素并删除
         *
         * @param key 键
         * @return
         */
        public static String popValue(String key) {
            return redisTemplate.opsForSet().pop(key).toString();
        }

        /**
         * 删除指定 hash 的 HashKey
         *
         * @param key
         * @param hashKeys
         * @return 删除成功的 数量
         */
        public static Long delete(String key, String... hashKeys) {
            return redisTemplate.opsForHash().delete(key, hashKeys);
        }

        /**
         * 给指定 hash 的 hashkey 做增减操作
         *
         * @param key
         * @param hashKey
         * @param number
         * @return
         */
        public static Long increment(String key, String hashKey, long number) {
            return redisTemplate.opsForHash().increment(key, hashKey, number);
        }

        /**
         * 给指定 hash 的 hashkey 做增减操作
         *
         * @param key
         * @param hashKey
         * @param number
         * @return
         */
        public static Double increment(String key, String hashKey, Double number) {
            return redisTemplate.opsForHash().increment(key, hashKey, number);
        }

        /**
         * 获取 key 下的 所有 hashkey 字段
         *
         * @param key
         * @return
         */
        public static Set hashKeys(String key) {
            return redisTemplate.opsForHash().keys(key);
        }

        /**
         * 获取指定 hash 下面的 键值对 数量
         *
         * @param key
         * @return
         */
        public static Long hashSize(String key) {
            return redisTemplate.opsForHash().size(key);
        }
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -
    public static class ListOps {
        /**
         * 在变量左边添加元素值
         *
         * @param key
         * @param value
         * @return
         */
        public static void leftPush(String key, Object value) {
            redisTemplate.opsForList().leftPush(key, value);
        }

        /**
         * 获取集合指定位置的值。
         *
         * @param key
         * @param index
         * @return
         */
        public static Object index(String key, long index) {
            return redisTemplate.opsForList().index("list", 1);
        }

        /**
         * 获取指定区间的值。
         *
         * @param key
         * @param start
         * @param end
         * @return
         */
        public static List range(String key, long start, long end) {
            return redisTemplate.opsForList().range(key, start, end);
        }

        /**
         * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，
         * 如果中间参数值存在的话。
         *
         * @param key
         * @param pivot
         * @param value
         * @return
         */
        public static void leftPush(String key, String pivot, String value) {
            redisTemplate.opsForList().leftPush(key, pivot, value);
        }

        /**
         * 向左边批量添加参数元素。
         *
         * @param key
         * @param values
         * @return
         */
        public static void leftPushAll(String key, String... values) {
//        redisTemplate.opsForList().leftPushAll(key,"w","x","y");
            redisTemplate.opsForList().leftPushAll(key, values);
        }

        public static void leftPushAll(String key, Collection collection) {
            redisTemplate.opsForList().leftPushAll(key, collection);
        }

        /**
         * 向集合最右边添加元素。
         *
         * @param key
         * @param value
         * @return
         */
        public static void leftPushAll(String key, String value) {
            redisTemplate.opsForList().rightPush(key, value);
        }

        /**
         * 向左边批量添加参数元素。
         *
         * @param key
         * @param values
         * @return
         */
        public static void rightPushAll(String key, String... values) {
            //redisTemplate.opsForList().leftPushAll(key,"w","x","y");
            redisTemplate.opsForList().rightPushAll(key, values);
        }

        /**
         * 向已存在的集合中添加元素。
         *
         * @param key
         * @param value
         * @return
         */
        public static void rightPushIfPresent(String key, Object value) {
            redisTemplate.opsForList().rightPushIfPresent(key, value);
        }

        /**
         * 向已存在的集合中添加元素。
         *
         * @param key
         * @return
         */
        public static long listLength(String key) {
            return redisTemplate.opsForList().size(key);
        }

        /**
         * 移除集合中的左边第一个元素。
         *
         * @param key
         * @return
         */
        public static void leftPop(String key) {
            redisTemplate.opsForList().leftPop(key);
        }

        /**
         * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
         *
         * @param key
         * @return
         */
        public static void leftPop(String key, long timeout, TimeUnit unit) {
            redisTemplate.opsForList().leftPop(key, timeout, unit);
        }

        /**
         * 移除集合中右边的元素。
         *
         * @param key
         * @return
         */
        public static void rightPop(String key) {
            redisTemplate.opsForList().rightPop(key);
        }

        /**
         * 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
         *
         * @param key
         * @return
         */
        public static void rightPop(String key, long timeout, TimeUnit unit) {
            redisTemplate.opsForList().rightPop(key, timeout, unit);
        }
    }

    public static class LockOps {
        /**
         * 加锁
         *
         * @param key
         * @param value
         * @param timeout 过期时间
         */
        public static boolean lock(String key, String value, Integer timeout) {
            return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
        }

        public static boolean lock(String key, String value, Integer timeout, TimeUnit timeUnit) {
            return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
        }


        /**
         * 释放锁
         *
         * @param key
         */
        public static boolean releaseLock(String key, String value) {
            if (StringUtil.isBlank(key) || StringUtil.isBlank(value))
                return false;
            boolean releaseLock = false;
            String requestId = (String) redisTemplate.opsForValue().get(key);
            if (value.equals(requestId)) {
                releaseLock = redisTemplate.delete(key);
            }
            return releaseLock;
        }


        /**
         * 获取(分布式)锁. 加锁，自旋重试三次
         *
         * @param key
         * @param value
         * @return
         */
        public static boolean getLock(String key, String value) {
            boolean locked = false;
            int tryCount = 3;
            while (!locked && tryCount > 0) {
                locked = redisTemplate.opsForValue().setIfAbsent(key, value, 2, TimeUnit.MINUTES);
                tryCount--;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    log.error("线程被中断" + Thread.currentThread().getId(), e);
                }
            }
            return locked;
        }
    }





}
