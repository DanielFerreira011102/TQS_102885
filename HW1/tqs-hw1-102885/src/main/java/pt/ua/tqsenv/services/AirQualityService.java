package pt.ua.tqsenv.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pt.ua.tqsenv.cache.Cache;
import pt.ua.tqsenv.entities.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirQualityService {
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
        String key = getKey(location);

        CurrentAirQualityData cachedData = (CurrentAirQualityData) cache.get(key);

        if (cachedData != null) {
            return cachedData;
        }

        CurrentAirQualityData fetchedData = fetchCurrentAirQualityData(location);
        cache.put(fetchedData, key);

        return fetchedData;
    }

    public ForecastAirQualityData getForecastAirQualityData(String location, String date, Boolean current, Integer days, Boolean hours) {
        String key = getKey(location, date, current, days, hours);

        ForecastAirQualityData cachedData = (ForecastAirQualityData) cache.get(key);

        if (cachedData != null) {
            return cachedData;
        }

        ForecastAirQualityData fetchedData = fetchForecastAirQualityData(location, date, current, days, hours);
        cache.put(fetchedData, key);

        return fetchedData;
    }

    public CacheAnalyticsData getCacheAnalyticsData() {
        return cache.getStats();
    }

    private CurrentAirQualityData fetchCurrentAirQualityData(String location) {
        // Create the API endpoint URL
        String apiUrl = String.format("%s/current.json?key=%s&q=%s&aqi=yes", weatherApiUrl, weatherApiKey, location);

        try {
            // Make the API request
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // Check if the API request was successful
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
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

            // Throw an exception if the API request was not successful
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve air quality data.");
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing JSON response from API.", e);
        }
        catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching location found.", e);
        }
    }

    private ForecastAirQualityData fetchForecastAirQualityData(String location, String date, Boolean includeCurrent, Integer forecastDays, Boolean includeHours) {
        // Create the API endpoint URL
        String apiUrl = String.format("%sforecast.json?key=%s&q=%s&aqi=yes&alerts=no%s",
                weatherApiUrl,
                weatherApiKey,
                location,
                date != null ? "&dt=" + date : "&days=" + forecastDays);

        try {
            // Make the API request
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // Check if the API request was successful
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Parse the JSON response into an AirQualityData object
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

                // Extract the required data from the JSON response
                JsonNode locationNode = rootNode.at("/location");

                // Create a new ForecastAirQualityData object with the extracted data
                ForecastAirQualityData forecastAirQualityData = new ForecastAirQualityData();
                forecastAirQualityData.setLocation(objectMapper.treeToValue(locationNode, WeatherLocation.class));

                if (date != null) {
                    JsonNode forecastAirQualityNode = rootNode.at("/forecast/forecastday/0/day/air_quality");
                    JsonNode conditionNode = rootNode.at("/forecast/forecastday/0/day/condition/text");
                    JsonNode dateEpochNode = rootNode.at("/forecast/forecastday/0/date_epoch");
                    JsonNode dateNode = rootNode.at("/forecast/forecastday/0/date");


                    forecastAirQualityData.setAirQuality(objectMapper.treeToValue(forecastAirQualityNode, WeatherAirQuality.class));
                    forecastAirQualityData.setCondition(conditionNode.asText());
                    forecastAirQualityData.setDateEpoch(dateEpochNode.asLong());
                    forecastAirQualityData.setDate(dateNode.asText());

                    if (includeHours) {
                        List<HourAirQualityData> hoursAirQualityData = new ArrayList<>();

                        JsonNode hourNodes = rootNode.at("/forecast/forecastday/0/hour");

                        addHourQualityDataList(forecastAirQualityData, hoursAirQualityData, hourNodes);
                    }
                }

                else {
                    List<ForecastAirQualityData> daysAirQualityData = new ArrayList<>();

                    JsonNode dayNodes = rootNode.at("/forecast/forecastday");

                    for (JsonNode day : dayNodes) {
                        JsonNode dayDateEpochNode = day.at("/date_epoch");
                        JsonNode dayDateNode = day.at("/date");
                        JsonNode dayConditionNode = day.at("/day/condition/text");
                        JsonNode dayAirQualityNode = day.at("/day/air_quality");

                        ForecastAirQualityData dayForecastAirQualityData = ForecastAirQualityData.builder()
                            .dateEpoch(dayDateEpochNode.asLong())
                            .date(dayDateNode.asText())
                            .condition(dayConditionNode.asText())
                            .airQuality(objectMapper.treeToValue(dayAirQualityNode, WeatherAirQuality.class)).build();

                        if (includeHours) {
                            List<HourAirQualityData> hoursAirQualityData = new ArrayList<>();

                            JsonNode hourNodes = day.at("/hour");

                            addHourQualityDataList(dayForecastAirQualityData, hoursAirQualityData, hourNodes);
                        }

                        daysAirQualityData.add(dayForecastAirQualityData);
                    }

                    forecastAirQualityData.setDays(daysAirQualityData);
                }

                if (includeCurrent) {
                    JsonNode currentAirQualityNode = rootNode.at("/current/air_quality");
                    JsonNode currentLastUpdatedEpochNode = rootNode.at("/current/last_updated_epoch");
                    JsonNode currentLastUpdatedNode = rootNode.at("/current/last_updated");
                    JsonNode currentConditionNode = rootNode.at("/current/condition/text");
                    JsonNode currentIsDayNode = rootNode.at("/current/is_day");

                    CurrentAirQualityData currentAirQualityData = CurrentAirQualityData.builder()
                        .airQuality(objectMapper.treeToValue(currentAirQualityNode, WeatherAirQuality.class))
                        .condition(currentConditionNode.asText())
                        .lastUpdatedEpoch(currentLastUpdatedEpochNode.asLong())
                        .lastUpdated(currentLastUpdatedNode.asText())
                        .isDay(currentIsDayNode.asBoolean()).build();

                    forecastAirQualityData.setCurrentAirQualityData(currentAirQualityData);
                }

                return forecastAirQualityData;
            }

            // Throw an exception if the API request was not successful
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve air quality data.");
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing JSON response from API.", e);
        }
        catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching location found.", e);
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