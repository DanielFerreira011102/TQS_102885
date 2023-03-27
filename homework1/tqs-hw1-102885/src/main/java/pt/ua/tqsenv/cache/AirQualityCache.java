package pt.ua.tqsenv.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ua.tqsenv.entities.AirQualityData;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class AirQualityCache {
    private final Map<String, CachedAirQualityData> cache = new HashMap<>();
    private int requestsCount = 0;
    private int cacheHits = 0;
    private int cacheMisses = 0;

    @Value("${cache.ttl.seconds}")
    private int cacheTTLSeconds;

    public AirQualityData get(String location, String date, Boolean current, Integer days, Boolean hours) {
        requestsCount++;

        String key = getKey(location, date, current, days, hours);

        CachedAirQualityData cachedData = cache.get(key);

        if (cachedData == null || cachedData.isExpired()) {

            if (cachedData != null) {
                cache.remove(key);
            }

            cacheMisses++;
            return null;
        }

        cacheHits++;
        return cachedData.getData();
    }

    public void put(AirQualityData data, String location, String date, Boolean current, Integer days, Boolean hours) {
        String key = getKey(location, date, current, days, hours);

        CachedAirQualityData cachedData = cache.get(key);

        if (cachedData != null) {
            cachedData.setTTL(Duration.ofSeconds(cacheTTLSeconds));
        }
        else {
            cachedData = new CachedAirQualityData(data, Duration.ofSeconds(cacheTTLSeconds));
            cache.put(key, cachedData);
        }
    }

    public Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("requestsCount", requestsCount);
        stats.put("cacheHits", cacheHits);
        stats.put("cacheMisses", cacheMisses);
        return stats;
    }

    public String getKey(String location, String date, Boolean current, Integer days, Boolean hours) {
        String key = location;

        if (date != null)
            key += "_" + date;
        else if (days != null)
            key += "_" + days;

        if (current != null)
            key += "_c" + current;

        if (hours != null)
            key += "_h" + hours;

        return key;
    }

    @Override
    public String toString() {
        return "AirQualityCache{" +
                "cache=" + cache +
                ", requestsCount=" + requestsCount +
                ", cacheHits=" + cacheHits +
                ", cacheMisses=" + cacheMisses +
                ", cacheTTLSeconds=" + cacheTTLSeconds +
                '}';
    }

    @Scheduled(fixedRateString = "${cache.ttl.seconds}") // Run every minute
    public void removeExpiredElements() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private static class CachedAirQualityData {
        private final AirQualityData data;
        private Duration ttl;
        private long expiresAt;

        private CachedAirQualityData(AirQualityData data, Duration ttl) {
            this.data = data;
            this.ttl = ttl;
            this.expiresAt = System.currentTimeMillis() + ttl.toMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expiresAt;
        }

        public AirQualityData getData() {
            return data;
        }

        public void setTTL(Duration ttl) {
            this.ttl = ttl;
            this.expiresAt = System.currentTimeMillis() + ttl.toMillis();
        }
    }
}
