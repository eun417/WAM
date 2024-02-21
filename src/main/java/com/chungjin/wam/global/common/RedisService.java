package com.chungjin.wam.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate template;

    /**
     * key로 value를 가져옴
     */
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 해당 key에 해당하는 value가 존재하는지 확인
     */
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    /**
     * 새로운 key - value 쌍을 저장
     */
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    /**
     * key에 해당하는 데이터를 지움
     */
    public void deleteData(String key) {
        template.delete(key);
    }

}
