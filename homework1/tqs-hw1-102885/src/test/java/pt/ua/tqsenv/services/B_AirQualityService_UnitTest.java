package pt.ua.tqsenv.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;
import pt.ua.tqsenv.cache.AirQualityCache;
import pt.ua.tqsenv.entities.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class B_AirQualityService_UnitTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AirQualityService airQualityService;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        AirQualityCache cache = new AirQualityCache();
        airQualityService = new AirQualityService(restTemplate, objectMapper, cache);
    }

    @Test
    @DisplayName("Trying to get the current air quality data of a location")
    public void whenResolveViseuCurrentUrl_returnViseuCurrentAirQualityData() throws Exception {
        // Setup
        String location = "Viseu";

        CurrentAirQualityData testData = CurrentAirQualityData.builder()
                .location(new WeatherLocation("Viseu", "Viseu", "Portugal", 40.65, -7.92, "Europe/Lisbon", 1680105392L, "2023-03-2916:56"))
                .airQuality(new WeatherAirQuality(196.89999389648438, 1.0, 125.9000015258789, 0.800000011920929, 5.199999809265137, 7.5, 1, 1))
                .condition("Sunny")
                .isDay(true)
                .lastUpdated("2023-03-2916:45")
                .lastUpdatedEpoch(1680104700L).build();

        String responseString = readFile("currentExpected.json");
        ResponseEntity<String> response = new ResponseEntity<>(responseString, HttpStatus.OK);

        // Mock
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(response);

        // Test
        CurrentAirQualityData result = airQualityService.getCurrentAirQualityData(location);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testData);

        CacheAnalyticsData stats = airQualityService.getCacheAnalyticsData();

        assertThat(stats.getRequestsCount()).isEqualTo(1);
        assertThat(stats.getCacheHits()).isEqualTo(0);
        assertThat(stats.getCacheMisses()).isEqualTo(1);
        assertThat(stats.getExpiredCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Trying to get the forecast air quality data of a location on a specific day")
    public void whenResolveViseuForecastUrlWithDate_returnViseuForecastAirQualityData() throws Exception {
        // Setup
        String location = "Viseu";
        String date = "2023-03-30";

        ForecastAirQualityData testData = ForecastAirQualityData.builder()
                .location(new WeatherLocation("Viseu", "Viseu", "Portugal", 40.65, -7.92, "Europe/Lisbon", 1680109415L, "2023-03-2918:03"))
                .airQuality(new WeatherAirQuality(200.27600158691405, 1.3040000152587892, 84.76399993896484, 0.5040000081062317, 4.0400000047683715, 9.011999931335449, 1, 1))
                .condition("Patchy rain possible")
                .date("2023-03-30")
                .dateEpoch(1680134400L).build();

        String responseString = readFile("forecastExpected.json");
        ResponseEntity<String> response = new ResponseEntity<>(responseString, HttpStatus.OK);

        // Mock
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(response);

        // Test
        ForecastAirQualityData result = airQualityService.getForecastAirQualityData(location, date, false, 1, false);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testData);

        CacheAnalyticsData stats = airQualityService.getCacheAnalyticsData();

        assertThat(stats.getRequestsCount()).isEqualTo(1);
        assertThat(stats.getCacheHits()).isEqualTo(0);
        assertThat(stats.getCacheMisses()).isEqualTo(1);
        assertThat(stats.getExpiredCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Trying to get the forecast air quality data of a location, including current data and hours")
    public void whenResolveViseuForecastUrlWithDateAndIncludeCurrentAndHours_returnViseuForecastAirQualityData() throws Exception {
        // Setup
        String location = "Viseu";
        String date = "2023-03-30";

        CurrentAirQualityData currentTestData = CurrentAirQualityData.builder()
                .airQuality(new WeatherAirQuality(195.3000030517578, 1.7000000476837158, 120.19999694824219, 0.800000011920929, 4.099999904632568, 8.300000190734863, 1, 1))
                .condition("Sunny")
                .isDay(true)
                .lastUpdated("2023-03-2918:00")
                .lastUpdatedEpoch(1680109200L).build();

        String[] tokens = readFile("tokens.txt").split(",");
        List<HourAirQualityData> hourAirQualityDataList = new ArrayList<>();

        for (int i = 0; i < tokens.length; i += 12) {
            HourAirQualityData data = HourAirQualityData.builder()
                    .airQuality(new WeatherAirQuality(
                            Double.parseDouble(tokens[i]),
                            Double.parseDouble(tokens[i+1]),
                            Double.parseDouble(tokens[i+2]),
                            Double.parseDouble(tokens[i+3]),
                            Double.parseDouble(tokens[i+4]),
                            Double.parseDouble(tokens[i+5]),
                            Integer.parseInt(tokens[i+6]),
                            Integer.parseInt(tokens[i+7])
                    ))
                    .time(tokens[i+8])
                    .timeEpoch(Long.parseLong(tokens[i+9]))
                    .isDay(Boolean.parseBoolean(tokens[i+10]))
                    .condition(tokens[i+11])
                    .build();

            hourAirQualityDataList.add(data);
        }

        ForecastAirQualityData testData = ForecastAirQualityData.builder()
                .location(new WeatherLocation("Viseu", "Viseu", "Portugal", 40.65, -7.92, "Europe/Lisbon", 1680109415L, "2023-03-2918:03"))
                .airQuality(new WeatherAirQuality(200.27600158691405, 1.3040000152587892, 84.76399993896484, 0.5040000081062317, 4.0400000047683715, 9.011999931335449, 1, 1))
                .condition("Patchy rain possible")
                .date("2023-03-30")
                .dateEpoch(1680134400L)
                .hours(hourAirQualityDataList)
                .currentAirQualityData(currentTestData).build();

        String responseString = readFile("forecastExpected.json");
        ResponseEntity<String> response = new ResponseEntity<>(responseString, HttpStatus.OK);

        // Mock
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(response);

        // Test
        ForecastAirQualityData result = airQualityService.getForecastAirQualityData(location, date, true, 1, true);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testData);

        CacheAnalyticsData stats = airQualityService.getCacheAnalyticsData();

        assertThat(stats.getRequestsCount()).isEqualTo(1);
        assertThat(stats.getCacheHits()).isEqualTo(0);
        assertThat(stats.getCacheMisses()).isEqualTo(1);
        assertThat(stats.getExpiredCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Trying to get the forecast air quality data of a location by days")
    public void whenResolveViseuForecastUrlWithForecastDays_returnViseuForecastAirQualityData() throws Exception {
        // Setup
        String location = "Viseu";

        String[] tokens = readFile("tokens.txt").split(",");
        List<HourAirQualityData> hourAirQualityDataList = new ArrayList<>();

        for (int i = 0; i < tokens.length; i += 12) {
            HourAirQualityData data = HourAirQualityData.builder()
                    .airQuality(new WeatherAirQuality(
                            Double.parseDouble(tokens[i]),
                            Double.parseDouble(tokens[i+1]),
                            Double.parseDouble(tokens[i+2]),
                            Double.parseDouble(tokens[i+3]),
                            Double.parseDouble(tokens[i+4]),
                            Double.parseDouble(tokens[i+5]),
                            Integer.parseInt(tokens[i+6]),
                            Integer.parseInt(tokens[i+7])
                    ))
                    .time(tokens[i+8])
                    .timeEpoch(Long.parseLong(tokens[i+9]))
                    .isDay(Boolean.parseBoolean(tokens[i+10]))
                    .condition(tokens[i+11])
                    .build();

            hourAirQualityDataList.add(data);
        }

        List<ForecastAirQualityData> dayAirQualityDataList = new ArrayList<>();

        ForecastAirQualityData dayAirQualityData = ForecastAirQualityData.builder()
                .hours(hourAirQualityDataList)
                .airQuality(new WeatherAirQuality(200.27600158691405, 1.3040000152587892, 84.76399993896484, 0.5040000081062317, 4.0400000047683715, 9.011999931335449, 1, 1))
                .condition("Patchy rain possible")
                .date("2023-03-30")
                .dateEpoch(1680134400L)
                .build();

        dayAirQualityDataList.add(dayAirQualityData);

        ForecastAirQualityData testData = ForecastAirQualityData.builder()
                .location(new WeatherLocation("Viseu", "Viseu", "Portugal", 40.65, -7.92, "Europe/Lisbon", 1680109415L, "2023-03-2918:03"))
                .days(dayAirQualityDataList)
                .build();

        String responseString = readFile("forecastExpected.json");
        ResponseEntity<String> response = new ResponseEntity<>(responseString, HttpStatus.OK);

        // Mock
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(response);

        // Test
        ForecastAirQualityData result = airQualityService.getForecastAirQualityData(location, null, false, 1, true);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testData);

        CacheAnalyticsData stats = airQualityService.getCacheAnalyticsData();

        assertThat(stats.getRequestsCount()).isEqualTo(1);
        assertThat(stats.getCacheHits()).isEqualTo(0);
        assertThat(stats.getCacheMisses()).isEqualTo(1);
        assertThat(stats.getExpiredCount()).isEqualTo(0);
    }

    public String readFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        InputStream inputStream = resource.getInputStream();
        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
        return new String(bdata, StandardCharsets.UTF_8);
    }
}