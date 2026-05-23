package com.lloyds.offers.notification.infrastructure.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class FrequencyCapService {

    private static final int MAX_DAILY = 5;
    private static final Duration COOLDOWN = Duration.ofMinutes(30);

    private final StringRedisTemplate redis;

    public FrequencyCapService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean isAllowed(String userId, String offerId) {
        String date = LocalDate.now().toString();
        String dailyKey = "cap:%s:%s:%s".formatted(userId, offerId, date);
        String cooldownKey = "cooldown:%s:%s".formatted(userId, offerId);

        if (Boolean.TRUE.equals(redis.hasKey(cooldownKey))) {
            return false;
        }

        String count = redis.opsForValue().get(dailyKey);
        return count == null || Integer.parseInt(count) < MAX_DAILY;
    }

    public void record(String userId, String offerId) {
        String date = LocalDate.now().toString();
        String dailyKey = "cap:%s:%s:%s".formatted(userId, offerId, date);
        String cooldownKey = "cooldown:%s:%s".formatted(userId, offerId);

        redis.opsForValue().increment(dailyKey);
        redis.expire(dailyKey, Duration.ofDays(1));
        redis.opsForValue().set(cooldownKey, "1", COOLDOWN);
    }

    public boolean check(String userId, String offerId) {
        return isAllowed(userId, offerId);
    }
}
