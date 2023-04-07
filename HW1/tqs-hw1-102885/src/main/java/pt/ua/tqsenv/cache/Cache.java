package pt.ua.tqsenv.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(Cache.class);

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

        if (cachedData == null) {
            cacheMisses++;
            logger.debug("Cache miss for key: {}", key);
            return null;
        }

        if (cachedData.isExpired()) {
            expiredCount++;
            cachedMap.remove(key);
            logger.debug("Removed expired data from cache for key: {}", key);
            cacheMisses++;
            return null;
        }

        cacheHits++;
        logger.debug("Cache hit for key: {}", key);
        return cachedData.getData();
    }

    public void put(T data, String key) {
        CachedData<T> cachedData = cachedMap.get(key);

        if (cachedData != null) {
            cachedData.setTTL(Duration.ofSeconds(cacheTTLSeconds));
            logger.debug("Refreshed data in cache for key: {}", key);
        } else {
            cachedData = new CachedData<>(data, Duration.ofSeconds(cacheTTLSeconds));
            cachedMap.put(key, cachedData);
            logger.debug("Added data to cache for key: {}", key);
        }
    }

    public CacheAnalyticsData getStats() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        logger.debug("Returning cache analytics data");
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
                logger.debug("Removed expired data from cache for key: {}", entry.getKey());
            }
        }
        expiredCount += removedCount;
        logger.debug("Removed {} expired elements from cache", removedCount);
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

