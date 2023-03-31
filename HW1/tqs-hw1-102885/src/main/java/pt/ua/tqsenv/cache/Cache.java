package pt.ua.tqsenv.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ua.tqsenv.entities.CacheAnalyticsData;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class Cache<T> {
    private final Map<String, CachedData<T>> cachedMap = new HashMap<>();
    private Integer requestsCount = 0;
    private Integer cacheHits = 0;
    private Integer cacheMisses = 0;
    private Integer expiredCount = 0;
    @Value("${cache.ttl.seconds:60}")
    private Integer cacheTTLSeconds = 60;

    public T get(String key) {
        requestsCount++;

        CachedData<T> cachedData = cachedMap.get(key);

        if (cachedData == null || cachedData.isExpired()) {

            if (cachedData != null) {
                expiredCount++;
                cachedMap.remove(key);
            }

            cacheMisses++;
            return null;
        }

        cacheHits++;
        return cachedData.getData();
    }

    public void put(T data, String key) {
        CachedData<T> cachedData = cachedMap.get(key);

        if (cachedData != null) {
            cachedData.setTTL(Duration.ofSeconds(cacheTTLSeconds));
        }
        else {
            cachedData = new CachedData<>(data, Duration.ofSeconds(cacheTTLSeconds));
            cachedMap.put(key, cachedData);
        }
    }

    public CacheAnalyticsData getStats() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        return new CacheAnalyticsData(formatter.format(date), requestsCount, cacheHits, cacheMisses, expiredCount);
    }

    public Integer getCacheTTLSeconds() {
        return cacheTTLSeconds;
    }

    @Scheduled(fixedRateString = "${cache.ttl.seconds:60}") // Run every minute
    private void removeExpiredElements() {
        int removedCount = 0;
        Iterator<Map.Entry<String, CachedData<T>>> iterator = cachedMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CachedData<T>> entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
                removedCount++;
            }
        }
        expiredCount += removedCount;
    }


    private static class CachedData<T> {
        private final T data;
        private long expiresAt;

        private CachedData(T data, Duration ttl) {
            this.data = data;
            this.expiresAt = System.currentTimeMillis() + ttl.toMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expiresAt;
        }

        public T getData() {
            return data;
        }

        public void setTTL(Duration ttl) {
            this.expiresAt = System.currentTimeMillis() + ttl.toMillis();
        }
    }
}

