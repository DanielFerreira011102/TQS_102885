package pt.ua.tqsenv.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.tqsenv.entities.*;
import pt.ua.tqsenv.services.AirQualityService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * WebMvcTest loads a simplified web environment for the tests. Note that the normal
 * auto-discovery of beans (and dependency injection) is limited
 * This strategy deploys the required components to a test-friendly web framework, that can be accessed
 * by injecting a MockMvc reference
 */
@WebMvcTest(AirQualityController.class)
class C_AirQualityController_WithMockServiceTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mvc;    //entry point to the web framework

    // inject required beans as "mockeable" objects
    // note that @AutoWire would result in NoSuchBeanDefinitionException
    @MockBean
    private AirQualityService service;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    @DisplayName("Trying to request the current air quality json data of a location")
    void whenGetCurrentAirQualityData_returnCurrentAirQualityDataJsonObject() throws Exception {

        CurrentAirQualityData testData = CurrentAirQualityData.builder()
                .location(new WeatherLocation("Viseu", "Viseu", "Portugal", 40.65, -7.92, "Europe/Lisbon", 1680105392L, "2023-03-2916:56"))
                .airQuality(new WeatherAirQuality(196.89999389648438, 1.0, 125.9000015258789, 0.800000011920929, 5.199999809265137, 7.5, 1, 1))
                .condition("Sunny")
                .isDay(true)
                .lastUpdated("2023-03-2916:45")
                .lastUpdatedEpoch(1680104700L).build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testData);

        when(service.getCurrentAirQualityData("Viseu")).thenReturn(testData);

        RestAssuredMockMvc.given()
                .auth().none()
                .contentType("application/json")
                .queryParam("location", "Viseu")
                .get("/api/v1/current")
                .then().statusCode(200)
                .body(equalTo(json));

        verify(service, times(1)).getCurrentAirQualityData("Viseu");
    }

    @Test
    @DisplayName("Trying to request the forecast air quality json data of a location on a specific day")
    void whenGetForecastAirQualityData_returnForecastAirQualityDataJsonObject() throws Exception {

        ForecastAirQualityData testData = ForecastAirQualityData.builder()
                .location(new WeatherLocation("Viseu", "Viseu", "Portugal", 40.65, -7.92, "Europe/Lisbon", 1680109415L, "2023-03-2918:03"))
                .airQuality(new WeatherAirQuality(200.27600158691405, 1.3040000152587892, 84.76399993896484, 0.5040000081062317, 4.0400000047683715, 9.011999931335449, 1, 1))
                .condition("Patchy rain possible")
                .date("2023-03-30")
                .dateEpoch(1680134400L).build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testData);

        when(service.getForecastAirQualityData("Viseu", "2023-03-30", false, 1, false)).thenReturn(testData);

        RestAssuredMockMvc.given()
                .auth().none()
                .contentType("application/json")
                .queryParam("location", "Viseu")
                .queryParam("date", "2023-03-30")
                .get("/api/v1/forecast")
                .then().statusCode(200)
                .body(equalTo(json));

        verify(service, times(1)).getForecastAirQualityData("Viseu", "2023-03-30", false, 1, false);
    }

    @Test
    @DisplayName("Trying to request the current cache analytics data")
    void whenGetCacheAnalyticsData_returnGetCacheAnalyticsDataJsonObject() throws Exception {

        CacheAnalyticsData testData = CacheAnalyticsData.builder()
                .requestsCount(3)
                .cacheHits(2)
                .cacheMisses(1)
                .expiredCount(0)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testData);

        when(service.getCacheAnalyticsData()).thenReturn(testData);

        RestAssuredMockMvc.given()
                .auth().none()
                .contentType("application/json")
                .get("/api/v1/cache")
                .then().statusCode(200)
                .body(equalTo(json));

        verify(service, times(1)).getCacheAnalyticsData();
    }
}