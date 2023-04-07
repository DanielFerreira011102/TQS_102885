package pt.ua.tqsenv.cache;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ua.tqsenv.entities.AirQualityData;
import pt.ua.tqsenv.entities.CacheAnalyticsData;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class A_AirQualityCache_UnitTest {

    @Mock
    private AirQualityData dataMock;

    private Cache<AirQualityData> cache;

    @BeforeEach
    void setUp() {
        cache = new Cache<>();
    }

    @AfterEach
    void tearDown() {
        cache = null;
    }

    @Test
    @DisplayName("Trying to get an element not in the cache")
    void whenGettingNonExistingElement_returnNullAndCacheMisses() {
        assertThat(cache.getStats().getCacheMisses()).isZero();

        AirQualityData result = cache.get("Viseu");

        assertThat(result).isNull();
        assertThat(cache.getStats().getCacheMisses()).isEqualTo(1);
    }

    @Test
    @DisplayName("Trying to get an element in the cache")
    void whenGettingExistingElement_returnElementAndCacheHits() {
        assertThat(cache.getStats().getCacheHits()).isZero();

        cache.put(dataMock, "Viseu");
        AirQualityData result = cache.get("Viseu");

        assertThat(result).isEqualTo(dataMock);
        assertThat(cache.getStats().getCacheHits()).isEqualTo(1);
    }

    @Test
    @DisplayName("Trying to get an expired element and refreshing TTL")
    //@Disabled("Takes 1 minute to complete")
    void whenGettingExpiredElement_returnNull() throws InterruptedException {
        Integer cacheTTL = cache.getCacheTTLSeconds();
        cache.put(dataMock, "Viseu");
        cache.put(dataMock, "Aveiro");

        AirQualityData result;

        await().pollDelay((long)(cacheTTL * 0.2), TimeUnit.SECONDS).atMost(cacheTTL, TimeUnit.SECONDS).until(() -> true);

        cache.put(dataMock, "Viseu");

        await().pollDelay((long)(cacheTTL * 0.8 + 1), TimeUnit.SECONDS).atMost(cacheTTL, TimeUnit.SECONDS).untilAsserted(() -> assertThat(cache.get("Viseu")).isEqualTo(dataMock));

        result = cache.get("Aveiro");
        assertThat(result).isNull();

        CacheAnalyticsData stats = cache.getStats();
        assertThat(stats.getExpiredCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Checking cache analytics")
    void whenGettingCacheStats_returnCorrectData() {
        cache.put(dataMock, "Viseu");
        cache.put(dataMock, "Lisbon:2023-03-29:false:4:true");
        cache.put(dataMock, "Madrid::true:1:false");

        cache.get("Viseu"); // 1, 1, 0, 0
        cache.get("Lisbon:2023-03-29:false:4:true"); // 2, 2, 0, 0
        cache.get("Madrid"); // 3, 2, 1, 0
        cache.get("Viseu"); // 4, 3, 1, 0

        cache.get("Berlin"); // 5, 3, 2, 0
        cache.get("Rome::true:2:true"); // 6, 3, 3, 0

        cache.put(dataMock, "Berlin");
        cache.get("Berlin"); // 7, 4, 3, 0

        CacheAnalyticsData stats = cache.getStats();

        assertThat(stats.getRequestsCount()).isEqualTo(7);
        assertThat(stats.getCacheHits()).isEqualTo(4);
        assertThat(stats.getCacheMisses()).isEqualTo(3);
        assertThat(stats.getExpiredCount()).isZero();
    }

    private void sleep(Double time) {
        await().pollDelay(Duration.ofSeconds(time.longValue())).atMost(Duration.ofSeconds(time.longValue())).until(() -> true);
    }
}