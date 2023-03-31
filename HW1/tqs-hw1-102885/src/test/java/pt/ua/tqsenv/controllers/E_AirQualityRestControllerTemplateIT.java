package pt.ua.tqsenv.controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E_AirQualityRestControllerTemplateIT {

    // will need to use the server port for the invocation url
    @LocalServerPort
    int localPortForTestServer;

    @Test
    @DisplayName("Trying to request the current air quality json data of a location")
    @Order(1)
    void whenGetCurrentAirQualityData_returnCurrentAirQualityDataJsonObject() {

        String endpoint = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(localPortForTestServer)
                .pathSegment("api", "v1", "current")
                .queryParam("location", "Viseu")
                .build()
                .toUriString();

        RestAssured.given()
                .auth().none()
                .contentType("application/json")
                .when().get(endpoint)
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

        String endpoint = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(localPortForTestServer)
                .pathSegment("api", "v1", "forecast")
                .queryParam("location", "Viseu")
                .queryParam("days", 2)
                .build()
                .toUriString();

        RestAssured.given()
                .auth().none()
                .contentType("application/json")
                .when().get(endpoint)
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

        String endpoint = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(localPortForTestServer)
                .pathSegment("api", "v1", "forecast")
                .queryParam("location", "Viseu")
                .queryParam("days", 2)
                .queryParam("current", true)
                .queryParam("hours", true)
                .build()
                .toUriString();

        RestAssured.given()
                .auth().none()
                .contentType("application/json")
                .when().get(endpoint)
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

        String endpoint = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(localPortForTestServer)
                .pathSegment("api", "v1", "cache")
                .build()
                .toUriString();

        RestAssured.given()
                .auth().none()
                .contentType("application/json")
                .when().get(endpoint)
                .then().statusCode(200)
                .body("request_count", is(3))
                .body("cache_hits", is(0))
                .body("cache_misses", is(3))
                .body("expired_count", is(0));
    }
}
