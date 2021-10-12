package com.tyu.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;


@Configuration
public class RedisClusterConfig {

    @Value("${redis.nodeCluters}")
    private String nodeCluters;

    @Value("${redis.hostName}")
    private String hostName;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.model}")
    private int model;


    @Value("${redis.maxRedirects}")
    private int maxRedirects;
    @Value("${redis.pool.blockWhenExhausted}")
    private boolean blockWhenExhausted;
    @Value("${redis.pool.maxTotal}")
    private int maxTotal;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.minIdle}")
    private int minIdle;
    @Value("${redis.pool.numTestsPerEvictionRun}")
    private int numTestsPerEvictionRun;
    @Value("${redis.pool.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${redis.pool.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${redis.pool.softMinEvictableIdleTimeMillis}")
    private int softMinEvictableIdleTimeMillis;
    @Value("${redis.pool.maxWaitMillis}")
    private int maxWaitMillis;
    @Value("${redis.pool.timeOut}")
    private int timeOut;

    @Bean(name = "jedisPoolConfig")
    JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig pool = new JedisPoolConfig();
        pool.setMaxIdle(maxIdle);
        pool.setMinIdle(minIdle);
        pool.setBlockWhenExhausted(blockWhenExhausted);
        pool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        pool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        pool.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        pool.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);

        pool.setMaxTotal(maxTotal);
        pool.setMaxWaitMillis(maxWaitMillis);
        return pool;
    }

    @Bean(name = "redisClusterConfiguration")
    public RedisClusterConfiguration getRedisClusterConfiguration() {
        RedisClusterConfiguration configuration = new RedisClusterConfiguration();
        Set<RedisNode> nodes = new HashSet<RedisNode>();

        String[] nodec=nodeCluters.split(",");
        for(int i=0;i<nodec.length;i++){
            String hostName=nodec[i].split(":")[0];
            int port=Integer.parseInt(nodec[i].split(":")[1]);
            RedisNode node = new RedisNode(hostName, port);
            nodes.add(node);
        }

        configuration.setClusterNodes(nodes);
        configuration.setMaxRedirects(maxRedirects);
        return configuration;
    }

    @Bean(name = "jedisConnectionFactory")
    JedisConnectionFactory jedisConnectionFactory(
            @Qualifier("redisClusterConfiguration") RedisClusterConfiguration clusterConfig,
            @Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

        //单机模式
        if(model == 1){
            jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
            jedisConnectionFactory.setHostName(hostName);
            jedisConnectionFactory.setPort(port);
        } else if(model == 2){

            //哨兵模式
        } else if(model == 3){
            //Cluster模式
            jedisConnectionFactory = new JedisConnectionFactory(clusterConfig, jedisPoolConfig);
        }
        //	判断密码是否存在，存在设置值
        checkPasswordIfNull(jedisConnectionFactory);
        return jedisConnectionFactory;
    }

    @Bean
    public JedisCluster getJedisCluster(@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        String[] nodec=nodeCluters.split(",");
        for(int i=0;i<nodec.length;i++){
            String hostName=nodec[i].split(":")[0];
            int port=Integer.parseInt(nodec[i].split(":")[1]);
            HostAndPort node = new HostAndPort(hostName, port);
            nodes.add(node);
        }

        if (!StringUtils.isBlank(password) && model == 3) {
            return new JedisCluster(nodes, timeOut, timeOut, 5, password, jedisPoolConfig);
        } else {
            return new JedisCluster(nodes, timeOut, jedisPoolConfig);
        }
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(
            @Qualifier("jedisConnectionFactory") JedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        @SuppressWarnings({ "rawtypes", "unchecked" })
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }



    /**
     * 校验password是否为空，不为空设置密码
     * @param jedisConnectionFactory
     */
    private void checkPasswordIfNull(JedisConnectionFactory jedisConnectionFactory) {
        if (!StringUtils.isBlank(password)) {
            jedisConnectionFactory.setPassword(password);
        }
    }
}





