package org.alwyn.shortlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "rBloomFilterConfiguration")
public class RBloomFilterConfiguration {
    @Bean
    public RBloomFilter<String> usernameBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> usernameBloomFilter = redissonClient.getBloomFilter("usernameBloomFilter");
        usernameBloomFilter.tryInit(100000000L, 0.001);
        return usernameBloomFilter;
    }
}
