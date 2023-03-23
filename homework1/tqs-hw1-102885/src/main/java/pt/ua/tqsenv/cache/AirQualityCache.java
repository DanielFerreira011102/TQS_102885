package pt.ua.tqsenv.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ua.tqsenv.domain.AirQualityData;

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

    public AirQualityData get(String city) {
        requestsCount++;

        CachedAirQualityData cachedData = cache.get(city);

        if (cachedData == null || cachedData.isExpired()) {

            if (cachedData != null) {
                cache.remove(city);
            }

            cacheMisses++;
            return null;
        }

        cacheHits++;
        return cachedData.getData();
    }

    public void put(String city, AirQualityData data) {
        CachedAirQualityData cachedData = cache.get(city);
        if (cachedData != null) {
            cachedData.setTTL(Duration.ofSeconds(cacheTTLSeconds));
        }
        else {
            cachedData = new CachedAirQualityData(data, Duration.ofSeconds(cacheTTLSeconds));
            cache.put(city, cachedData);
        }
    }

    public Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("requestsCount", requestsCount);
        stats.put("cacheHits", cacheHits);
        stats.put("cacheMisses", cacheMisses);
        return stats;
    }

    public Map<String, CachedAirQualityData> getCache() {
        return cache;
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
