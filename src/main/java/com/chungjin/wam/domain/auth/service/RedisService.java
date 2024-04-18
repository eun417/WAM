package com.chungjin.wam.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
     * key로 value(List)를 가져옴
     */
    public List<String> getListData(String key) {
        ListOperations<String, String> listOperations = template.opsForList();
        return listOperations.range(key, 0, -1);
    }

    /**
     * 주어진 key가 있는지 확인
     * @param key
     * @return Redis에 key가 있다면 True
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
     * 새로운 key - value(List) 쌍을 저장
     */
    public void setListDataExpire(String key, List<String> values, long duration) {
        ListOperations<String, String> listOperations = template.opsForList();
        for (String value : values) {
            listOperations.rightPush(key, value);
        }
        template.expire(key, duration, TimeUnit.SECONDS);
    }

    /**
     * key에 해당하는 데이터를 지움
     */
    public void deleteData(String key) {
        template.delete(key);
    }

    /**
     * 입력받은 값과 저장된 데이터 비교
     * @param value
     * @return null이 아니고, 비어 있지 않으면 True
     */
    public boolean checkExistsValue(String value) {
        return value != null && !value.isEmpty();
    }

}
