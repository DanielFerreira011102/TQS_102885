package pt.ua.tqsenv.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pt.ua.tqsenv.cache.Cache;
import pt.ua.tqsenv.entities.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirQualityService {

    Logger logger = LoggerFactory.getLogger(AirQualityService.class);

    @Value("${weather.api.url}")
    private String weatherApiUrl;
    @Value("${weather.api.key}")
    private String weatherApiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Cache<AirQualityData> cache;

    public AirQualityService(RestTemplate restTemplate, ObjectMapper objectMapper, Cache<AirQualityData> cache) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.cache = cache;
    }

    public CurrentAirQualityData getCurrentAirQualityData(String location) {
        logger.info("Calculating cache access key for location {}", location);

        String key = getKey(location);

        logger.info("Trying to fetch current air quality data from cache for location {}", location);

        CurrentAirQualityData cachedData = (CurrentAirQualityData) cache.get(key);

        if (cachedData != null) {
            logger.info("Successfully fetched current air quality data from cache for location {}", location);
            return cachedData;
        }

        logger.info("Trying to fetch current air quality data from API for location {}", location);

        CurrentAirQualityData fetchedData = fetchCurrentAirQualityData(location);

        logger.info("Successfully fetched current air quality data from API for location {}", location);

        logger.info("Caching current air quality data for location {}", location);

        cache.put(fetchedData, key);

        return fetchedData;
    }

    public ForecastAirQualityData getForecastAirQualityData(String location, String date, Boolean current, Integer days, Boolean hours) {
        logger.info("Calculating cache access key for location {} with date {}, current={}, days={}, hours={}",
                location, date, current, days, hours);

        String key = getKey(location, date, current, days, hours);

        logger.info("Trying to fetch forecast air quality data from cache for location {} with date {}, current={}, days={}, hours={}",
                location, date, current, days, hours);

        ForecastAirQualityData cachedData = (ForecastAirQualityData) cache.get(key);

        if (cachedData != null) {
            logger.info("Successfully fetched forecast air quality data from cache for location {} with date {}, current={}, days={}, hours={}",
                    location, date, current, days, hours);
            return cachedData;
        }

        logger.info("Trying to fetch forecast air quality data from API for location {} with date {}, current={}, days={}, hours={}",
                location, date, current, days, hours);

        ForecastAirQualityData fetchedData = fetchForecastAirQualityData(location, date, current, days, hours);

        logger.info("Successfully fetched forecast air quality data from API for location {} with date {}, current={}, days={}, hours={}",
                location, date, current, days, hours);

        logger.info("Caching forecast air quality data for location {} with date {}, current={}, days={}, hours={}",
                location, date, current, days, hours);

        cache.put(fetchedData, key);

        return fetchedData;
    }

    public CacheAnalyticsData getCacheAnalyticsData() {
        logger.info("Retrieving cache analytics data");
        return cache.getStats();
    }

    private CurrentAirQualityData fetchCurrentAirQualityData(String location) {
        // Create the API endpoint URL
        String apiUrl = String.format("%s/current.json?key=%s&q=%s&aqi=yes", weatherApiUrl, weatherApiKey, location);

        try {
            // Make the API request
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // Parse the JSON response into an AirQualityData object
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

            // Extract the required data from the JSON response
            JsonNode locationNode = rootNode.at("/location");
            JsonNode currentAirQualityNode = rootNode.at("/current/air_quality");
            JsonNode lastUpdatedEpochNode = rootNode.at("/current/last_updated_epoch");
            JsonNode lastUpdatedNode = rootNode.at("/current/last_updated");
            JsonNode conditionNode = rootNode.at("/current/condition/text");
            JsonNode isDayNode = rootNode.at("/current/is_day");

                // Create a new CurrentAirQualityData object with the extracted data

            return CurrentAirQualityData.builder()
                    .location(objectMapper.treeToValue(locationNode, WeatherLocation.class))
                    .airQuality(objectMapper.treeToValue(currentAirQualityNode, WeatherAirQuality.class))
                    .condition(conditionNode.asText())
                    .lastUpdatedEpoch(lastUpdatedEpochNode.asLong())
                    .lastUpdated(lastUpdatedNode.asText())
                    .isDay(isDayNode.asBoolean()).build();
        }
        catch (JsonProcessingException e) {
            String errorMessage = "Error parsing JSON current response from API for location: " + location;
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
        catch (RestClientException e) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                HttpClientErrorException httpError = (HttpClientErrorException) e;
                JsonNode root = mapper.readTree(httpError.getResponseBodyAsString());
                String errorMessage = root.path("error").path("message").asText();
                throw new ResponseStatusException(httpError.getStatusCode(), errorMessage, e);
            } catch (JsonProcessingException ex) {
                String errorMessage = "Error parsing JSON current error response from API for location: " + location;
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, ex);
            }
        }
    }



    private ForecastAirQualityData fetchForecastAirQualityData(String location, String date, boolean includeCurrent, int forecastDays, boolean includeHours) {
        // Create the API endpoint URL
        String apiUrl = String.format("%sforecast.json?key=%s&q=%s&aqi=yes&alerts=no%s",
                weatherApiUrl,
                weatherApiKey,
                location,
                date != null ? "&dt=" + date : "&days=" + forecastDays);

        try {
            // Make the API request
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // Parse the JSON response into an AirQualityData object
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

            // Extract the required data from the JSON response
            JsonNode locationNode = rootNode.at("/location");

            // Create a new ForecastAirQualityData object with the extracted data
            ForecastAirQualityData forecastAirQualityData = new ForecastAirQualityData();

            forecastAirQualityData.setLocation(objectMapper.treeToValue(locationNode, WeatherLocation.class));

            if (date != null) {
                JsonNode dayNode = rootNode.at("/forecast/forecastday/0");
                parseForecastDayNode(dayNode, objectMapper, forecastAirQualityData, includeHours);
            } else {
                List<ForecastAirQualityData> daysAirQualityData = new ArrayList<>();
                for (JsonNode dayNode : rootNode.at("/forecast/forecastday")) {
                    ForecastAirQualityData dayForecastAirQualityData = new ForecastAirQualityData();
                    parseForecastDayNode(dayNode, objectMapper, dayForecastAirQualityData, includeHours);
                    daysAirQualityData.add(dayForecastAirQualityData);
                }
                forecastAirQualityData.setDays(daysAirQualityData);
            }

            if (includeCurrent) {
                CurrentAirQualityData currentAirQualityData = new CurrentAirQualityData();
                JsonNode currentAirQualityNode = rootNode.at("/current/air_quality");
                currentAirQualityData.setAirQuality(objectMapper.treeToValue(currentAirQualityNode, WeatherAirQuality.class));
                currentAirQualityData.setCondition(rootNode.at("/current/condition/text").asText());
                currentAirQualityData.setLastUpdatedEpoch(rootNode.at("/current/last_updated_epoch").asLong());
                currentAirQualityData.setLastUpdated(rootNode.at("/current/last_updated").asText());
                currentAirQualityData.setIsDay(rootNode.at("/current/is_day").asBoolean());
                forecastAirQualityData.setCurrentAirQualityData(currentAirQualityData);
            }

            return forecastAirQualityData;
        }
        catch (JsonProcessingException e) {
            String errorMessage = "Error parsing JSON forecast response from API for location: " + location;
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
        catch (RestClientException e) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                HttpClientErrorException httpError = (HttpClientErrorException) e;
                JsonNode root = mapper.readTree(httpError.getResponseBodyAsString());
                String errorMessage = root.path("error").path("message").asText();
                throw new ResponseStatusException(httpError.getStatusCode(), errorMessage, e);
            } catch (JsonProcessingException ex) {
                String errorMessage = "Error parsing JSON current error response from API for location: " + location;
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, ex);
            }
        }
    }

    private void parseForecastDayNode(JsonNode dayNode, ObjectMapper objectMapper, ForecastAirQualityData forecastAirQualityData, boolean includeHours) throws JsonProcessingException {
        JsonNode forecastAirQualityNode = dayNode.at("/day/air_quality");
        JsonNode conditionNode = dayNode.at("/day/condition/text");
        JsonNode dateEpochNode = dayNode.at("/date_epoch");
        JsonNode dateNode = dayNode.at("/date");

        forecastAirQualityData.setAirQuality(objectMapper.treeToValue(forecastAirQualityNode, WeatherAirQuality.class));
        forecastAirQualityData.setCondition(conditionNode.asText());
        forecastAirQualityData.setDateEpoch(dateEpochNode.asLong());
        forecastAirQualityData.setDate(dateNode.asText());

        if (includeHours) {
            List<HourAirQualityData> hoursAirQualityData = new ArrayList<>();
            JsonNode hourNodes = dayNode.at("/hour");
            addHourQualityDataList(forecastAirQualityData, hoursAirQualityData, hourNodes);
        }
    }


    private void addHourQualityDataList(ForecastAirQualityData forecastAirQualityData, List<HourAirQualityData> hoursAirQualityData, JsonNode hourNodes) throws JsonProcessingException {
        for (JsonNode hour : hourNodes) {
            JsonNode hourTimeEpoch = hour.at("/time_epoch");
            JsonNode hourTime = hour.at("/time");
            JsonNode hourDayNode = hour.at("/is_day");
            JsonNode hourConditionNode = hour.at("/condition/text");
            JsonNode hourAirQualityNode = hour.at("/air_quality");

            HourAirQualityData hourAirQualityData = HourAirQualityData.builder()
                .airQuality(objectMapper.treeToValue(hourAirQualityNode, WeatherAirQuality.class))
                .timeEpoch(hourTimeEpoch.asLong())
                .time(hourTime.asText())
                .condition(hourConditionNode.asText())
                .isDay(hourDayNode.asBoolean()).build();

            hoursAirQualityData.add(hourAirQualityData);
        }

        forecastAirQualityData.setHours(hoursAirQualityData);
    }

    private String getKey(String location, Object... args) {
        StringBuilder key = new StringBuilder(location);

        for (Object arg : args) {
            key.append(":");
            if (arg != null) {
                key.append(arg);
            }
        }

        return key.toString();
    }
}
