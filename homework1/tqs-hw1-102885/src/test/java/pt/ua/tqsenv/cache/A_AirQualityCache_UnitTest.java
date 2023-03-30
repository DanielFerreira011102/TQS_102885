package pt.ua.tqsenv.cache;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ua.tqsenv.entities.AirQualityData;
import pt.ua.tqsenv.entities.CacheAnalyticsData;
import java.util.concurrent.CountDownLatch;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class A_AirQualityCache_UnitTest {

    @Mock
    private AirQualityData dataMock;

    private AirQualityCache cache;

    @BeforeEach
    void setUp() {
        cache = new AirQualityCache();
    }

    @AfterEach
    public void tearDown() {
        cache = null;
    }

    @Test
    @DisplayName("Trying to get an element not in the cache")
    public void whenGettingNonExistingElement_returnNullAndCacheMisses() {
        assertThat(cache.getStats().getCacheMisses()).isEqualTo(0);

        AirQualityData result = cache.get("Viseu", null, null, null, null);

        assertThat(result).isNull();
        assertThat(cache.getStats().getCacheMisses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Trying to get an element in the cache")
    public void whenGettingExistingElement_returnElementAndCacheHits() {
        assertThat(cache.getStats().getCacheHits()).isEqualTo(0);

        cache.put(dataMock, "Viseu", null, null, null, null);
        AirQualityData result = cache.get("Viseu", null, null, null, null);

        assertThat(result).isEqualTo(dataMock);
        assertThat(cache.getStats().getCacheHits()).isEqualTo(1);
    }

    @Test
    @DisplayName("Trying to get an expired element and refreshing TTL")
    @Disabled("Takes 1 minute to complete")
    public void whenGettingExpiredElement_returnNull() throws InterruptedException {
        Integer cacheTTL = cache.getCacheTTLSeconds();
        cache.put(dataMock, "Viseu", null, null, null, null);
        cache.put(dataMock, "Aveiro", null, null, null, null);

        AirQualityData result;

        sleep(cacheTTL * 0.2);

        cache.put(dataMock, "Viseu", null, null, null, null);

        sleep(cacheTTL * 0.8 + 1);

        result = cache.get("Viseu", null, null, null, null);
        assertThat(result).isEqualTo(dataMock);

        result = cache.get("Aveiro", null, null, null, null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Checking cache analytics")
    public void whenGettingCacheStats_returnCorrectData() {
        cache.put(dataMock, "Viseu", null, null, null, null);
        cache.put(dataMock, "Lisbon", "2023-03-29", false, 4, true);
        cache.put(dataMock, "Madrid", null, true, 1, false);

        cache.get("Viseu", null, null, null, null); // 1, 1, 0, 0
        cache.get("Lisbon", "2023-03-29", false, 4, true); // 2, 2, 0, 0
        cache.get("Madrid", null, null, null, null); // 3, 2, 1, 0
        cache.get("Viseu", null, null, null, null); // 4, 3, 1, 0

        cache.get("Berlin", null, null, null, null); // 5, 3, 2, 0
        cache.get("Rome", null, true, 2, true); // 6, 3, 3, 0

        cache.put(dataMock, "Berlin", null, null, null, null);
        cache.get("Berlin", null, null, null, null); // 7, 4, 3, 0

        CacheAnalyticsData stats = cache.getStats();

        assertThat(stats.getRequestsCount()).isEqualTo(7);
        assertThat(stats.getCacheHits()).isEqualTo(4);
        assertThat(stats.getCacheMisses()).isEqualTo(3);
        assertThat(stats.getExpiredCount()).isEqualTo(0);
    }

    private void sleep(Double time) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Thread.sleep((long) (time * 1000L)); // wait for cache to expire
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        latch.await();
    }
}