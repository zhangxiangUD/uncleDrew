package com.dakun.jianzhong.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * 保存登录用户到 redis, 以便其他微服务模块调用获取登录用户信息
 * <p>User: wangjie
 * <p>Date: 1/8/2018
 * @author wangjie
 */
@Service
public class LoginUserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RedisTemplate<String, LoginUser> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, LoginUser> userRedisTemplate = new RedisTemplate<>();
        userRedisTemplate.setKeySerializer(new StringRedisSerializer());
        userRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(LoginUser.class));
        userRedisTemplate.setConnectionFactory(connectionFactory);

        this.redisTemplate = userRedisTemplate;
    }

    public void putLoginUser(LoginUser user) {

    }

    private String getRedisKey(LoginUser loginUser) {
        return "login:user:" + loginUser.getAccountType() + ":" + loginUser.getUserId();
    }
}
