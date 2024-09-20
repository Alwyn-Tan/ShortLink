package org.alwyn.shortlink.project.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value="rBloomFilterConfiguration")
public class RBloomFilterConfiguration {
    @Bean
    public RBloomFilter<String> userRegisterBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> userRegisterBloomFilter = redissonClient.getBloomFilter("userRegisterBloomFilter");
        userRegisterBloomFilter.tryInit(100000000L, 0.001);
        return userRegisterBloomFilter;
    }
}
