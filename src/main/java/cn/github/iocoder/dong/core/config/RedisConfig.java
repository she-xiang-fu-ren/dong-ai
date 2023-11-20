package cn.github.iocoder.dong.core.config;

import cn.github.iocoder.dong.core.utils.RedisClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
@Configuration
public class RedisConfig {

    public RedisConfig(RedisTemplate<String, String> redisTemplate) {
        RedisClient.register(redisTemplate);
    }
}
