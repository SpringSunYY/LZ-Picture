package com.lz.common;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * redis测试类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-16  14:41
 * @Version: 1.0
 */
@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisZSet() {
        String keyStart = "testZst";
        String[] values = {
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        };
        //获取当前毫秒值
        long start = System.currentTimeMillis();
        //获取五秒后毫秒值
        long millis = 5000;
        long end = start + millis;
        String key = keyStart + ":" + start + "-" + end;
        // 使用数组长度限制循环次数，防止越界
        for (int i = 0; i < 1000000; i++) {
            //获取当前毫秒值
            long now = System.currentTimeMillis();
            //判断是否大于end,大于则重建key
            if (now >= end) {
                end = now + millis;
                key = keyStart + ":" + now + "-" + end;
            }
            String value = values[i % values.length];
//            System.out.println("value = " + value);
            redisTemplate.opsForZSet().incrementScore(key, value, i);
        }
        //获取前三名
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().rangeWithScores(key, 0, 30);
        if (rangeWithScores != null) {
            for (ZSetOperations.TypedTuple<String> tuple : rangeWithScores) {
                System.out.println("tuple.getValue() = " + tuple.getValue());
                System.out.println("tuple.getScore() = " + tuple.getScore());
            }
        }
    }
}
