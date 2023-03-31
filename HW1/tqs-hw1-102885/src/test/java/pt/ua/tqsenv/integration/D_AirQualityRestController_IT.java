package pt.ua.tqsenv.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.tqsenv.AirQualityApplication;

import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AirQualityApplication.class)
@AutoConfigureMockMvc
public class D_AirQualityRestController_IT {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    @DisplayName("Trying to request the current air quality json data of a location")
    @Order(1)
    void whenGetCurrentAirQualityData_returnCurrentAirQualityDataJsonObject() {

        RestAssuredMockMvc.given()
                .auth().none()
                .contentType("application/json")
                .queryParam("location", "Viseu")
                .get("/api/v1/current")
                .then().statusCode(200)
                .body("", hasKey("location"))
                .body("", hasKey("condition"))
                .body("", hasKey("air_quality"))
                .body("", hasKey("is_day"))
                .body("", hasKey("last_updated_epoch"))
                .body("", hasKey("last_updated"))
                .body("location.name", is("Viseu"))
                .body("condition", not(emptyOrNullString()))
                .body("location.lat", is(40.65F))
                .body("location.lon", is(-7.92F))
                .body("air_quality", notNullValue())
                .body("air_quality.pm2_5", greaterThan(0F))
                .body("air_quality.'us-epa-index'", isA(Integer.class))
                .body("air_quality.'gb-defra-index'", isA(Integer.class))
                .body("last_updated", matchesRegex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Trying to request the forecast air quality json data of a location on a specific day")
    @Order(2)
    void whenGetForecastAirQualityData_returnForecastAirQualityDataJsonObject() {

        RestAssuredMockMvc.given()
                .auth().none()
                .contentType("application/json")
                .queryParam("location", "Viseu")
                .queryParam("days", 2)
                .get("/api/v1/forecast")
                .then().statusCode(200)
                .body("", hasKey("location"))
                .body("", hasKey("days"))
                .body("location.name", is("Viseu"))
                .body("days", hasSize(2))
                .body("days[0].condition", notNullValue())
                .body("days[0].air_quality", notNullValue())
                .body("days[0].date_epoch", greaterThan(0))
                .body("days[0].date", notNullValue())
                .body("days[1].condition", notNullValue())
                .body("days[1].air_quality", notNullValue())
                .body("days[1].date_epoch", greaterThan(0))
                .body("days[1].date", notNullValue())
                .body("days[0].air_quality.co", greaterThan(0F))
                .body("days[0].air_quality.no2", greaterThan(0F))
                .body("days[0].air_quality.o3", greaterThan(0F))
                .body("days[0].air_quality.so2", greaterThan(0F))
                .body("days[0].air_quality.pm2_5", greaterThan(0F))
                .body("days[0].air_quality.pm10", greaterThan(0F))
                .body("days[0].air_quality.us-epa-index", isA(Integer.class))
                .body("days[0].air_quality.gb-defra-index", isA(Integer.class))
                .body("days[1].air_quality.co", greaterThan(0F))
                .body("days[1].air_quality.no2", greaterThan(0F))
                .body("days[1].air_quality.o3", greaterThan(0F))
                .body("days[1].air_quality.so2", greaterThan(0F))
                .body("days[1].air_quality.pm2_5", greaterThan(0F))
                .body("days[1].air_quality.pm10", greaterThan(0F))
                .body("days[1].air_quality.us-epa-index", isA(Integer.class))
                .body("days[1].air_quality.gb-defra-index", isA(Integer.class));
    }

    @Test
    @DisplayName("Trying to request the forecast air quality json data of a location on a specific day, including current data and hours")
    @Order(3)
    void whenGetForecastAirQualityDataWithCurrentAndHours_returnForecastAirQualityDataJsonObjectWithCurrentAndHours() {

        RestAssuredMockMvc.given()
                .auth().none()
                .contentType("application/json")
                .queryParam("location", "Viseu")
                .queryParam("days", 2)
                .queryParam("current", true)
                .queryParam("hours", true)
                .get("/api/v1/forecast")
                .then().statusCode(200)
                .body("", hasKey("location"))
                .body("", hasKey("days"))
                .body("", hasKey("current"))
                .body("location.name", is("Viseu"))
                .body("current", not(empty()))
                .body("current.last_updated", matchesRegex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))
                .body("current.air_quality", notNullValue())
                .body("current.air_quality.so2", greaterThan(0F))
                .body("current.air_quality.'gb-defra-index'", isA(Integer.class))
                .body("days", hasSize(2))
                .body("days[0].air_quality", notNullValue())
                .body("days[1].air_quality", notNullValue())
                .body("days[0]", hasKey("hours"))
                .body("days[1]", hasKey("hours"))
                .body("days[0].hours", not(empty()))
                .body("days[1].hours", not(empty()))
                .body("days[0].hours[9].air_quality", notNullValue())
                .body("days[0].hours[3].air_quality", notNullValue())
                .body("days[1].hours[5].air_quality", notNullValue())
                .body("days[1].hours[23].air_quality", notNullValue())
                .body("days[0].hours[13].time", notNullValue())
                .body("days[1].hours[0].time", notNullValue());
    }

    @Test
    @DisplayName("Trying to request the current cache analytics data")
    @Order(4)
    void whenGetCacheAnalyticsData_returnGetCacheAnalyticsDataJsonObject() {

        RestAssuredMockMvc.given()
                .auth().none()
                .contentType("application/json")
                .get("/api/v1/cache")
                .then().statusCode(200)
                .body("request_count", is(3))
                .body("cache_hits", is(0))
                .body("cache_misses", is(3))
                .body("expired_count", is(0));
    }
}
