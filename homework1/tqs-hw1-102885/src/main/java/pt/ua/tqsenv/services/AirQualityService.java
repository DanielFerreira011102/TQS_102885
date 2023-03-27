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
import pt.ua.tqsenv.cache.AirQualityCache;
import pt.ua.tqsenv.entities.*;
import pt.ua.tqsenv.exceptions.AirQualityServiceException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirQualityService {

    private final RestTemplate restTemplate;
    private final String weatherApiUrl;
    private final String weatherApiKey;
    private final ObjectMapper objectMapper;
    private final AirQualityCache cache;

    public AirQualityService(RestTemplate restTemplate, ObjectMapper objectMapper, AirQualityCache cache, @Value("${weather.api.url}") String weatherApiUrl, @Value("${weather.api.key}") String weatherApiKey) {
        this.restTemplate = restTemplate;
        this.weatherApiUrl = weatherApiUrl;
        this.weatherApiKey = weatherApiKey;
        this.objectMapper = objectMapper;
        this.cache = cache;
    }

    public CurrentAirQualityData getCurrentAirQualityData(String location) throws AirQualityServiceException {
        CurrentAirQualityData cachedData = (CurrentAirQualityData) cache.get(location, null, null, null, null);

        if (cachedData != null) {
            return cachedData;
        }

        CurrentAirQualityData fetchedData = fetchCurrentAirQualityData(location);
        cache.put(fetchedData, location, null, null, null, null);

        return fetchedData;
    }

    public ForecastAirQualityData getForecastAirQualityData(String location, String date, Boolean current, Integer days, Boolean hours) throws AirQualityServiceException {
        ForecastAirQualityData cachedData = (ForecastAirQualityData) cache.get(location, date, current, days, hours);

        if (cachedData != null) {
            return cachedData;
        }

        ForecastAirQualityData fetchedData = fetchForecastAirQualityData(location, date, current, days, hours);
        cache.put(fetchedData, location, date, current, days, hours);

        return fetchedData;
    }

    private CurrentAirQualityData fetchCurrentAirQualityData(String location) throws AirQualityServiceException {
        // Create the API endpoint URL
        String apiUrl = weatherApiUrl + "current.json?key=" + weatherApiKey + "&q=" + location + "&aqi=yes";

        try {
            // Make the API request
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // Check if the API request was successful
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Parse the JSON response into an AirQualityData object
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

                // Extract the required data from the JSON response
                JsonNode locationNode = rootNode.get("location");
                JsonNode currentAirQualityNode = rootNode.path("current").path("air_quality");
                JsonNode lastUpdatedEpochNode = rootNode.path("current").path("last_updated_epoch");
                JsonNode lastUpdatedNode = rootNode.path("current").path("last_updated");
                JsonNode conditionNode = rootNode.path("current").path("condition").path("text");
                JsonNode isDayNode = rootNode.path("current").path("is_day");

                // Create a new CurrentAirQualityData object with the extracted data
                CurrentAirQualityData currentAirQualityData = new CurrentAirQualityData();
                currentAirQualityData.setLocation(objectMapper.treeToValue(locationNode, WeatherLocation.class));
                currentAirQualityData.setAirQuality(objectMapper.treeToValue(currentAirQualityNode, WeatherAirQuality.class));
                currentAirQualityData.setCondition(conditionNode.asText());
                currentAirQualityData.setLastUpdatedEpoch(lastUpdatedEpochNode.asLong());
                currentAirQualityData.setLastUpdated(lastUpdatedNode.asText());
                currentAirQualityData.setIsDay(isDayNode.asBoolean());

                return currentAirQualityData;
            }

            // Throw an exception if the API request was not successful
            throw new AirQualityServiceException("Failed to retrieve air quality data");
        }
        catch (JsonProcessingException e) {
            throw new AirQualityServiceException("Error parsing JSON response from OpenAQ API", e);
        }
        catch (RestClientException e) {
            throw new AirQualityServiceException("Error making API request to OpenAQ API", e);
        }
    }

    private ForecastAirQualityData fetchForecastAirQualityData(String location, String date, Boolean includeCurrent, Integer forecastDays, Boolean includeHours) throws AirQualityServiceException {
        // Create the API endpoint URL
        String apiUrl =
                weatherApiUrl + "forecast.json?key=" + weatherApiKey + "&q=" + location + "&aqi=yes"
                + "&alerts=no" + (date != null? "&dt=" + date : "&days=" + forecastDays);

        try {
            // Make the API request
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            // Check if the API request was successful
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Parse the JSON response into an AirQualityData object
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

                // Extract the required data from the JSON response
                JsonNode locationNode = rootNode.get("location");

                // Create a new ForecastAirQualityData object with the extracted data
                ForecastAirQualityData forecastAirQualityData = new ForecastAirQualityData();
                forecastAirQualityData.setLocation(objectMapper.treeToValue(locationNode, WeatherLocation.class));

                if (date != null) {
                    JsonNode forecastAirQualityNode = rootNode.path("forecast").path("forecastday").get(0).path("day").path("air_quality");
                    JsonNode conditionNode = rootNode.path("forecast").path("forecastday").get(0).path("day").path("condition").path("text");
                    JsonNode dateEpochNode = rootNode.path("forecast").path("forecastday").get(0).path("date_epoch");
                    JsonNode dateNode = rootNode.path("forecast").path("forecastday").get(0).path("date");
                    forecastAirQualityData.setAirQuality(objectMapper.treeToValue(forecastAirQualityNode, WeatherAirQuality.class));
                    forecastAirQualityData.setCondition(conditionNode.asText());
                    forecastAirQualityData.setDateEpoch(dateEpochNode.asLong());
                    forecastAirQualityData.setDate(dateNode.asText());

                    if (includeHours) {
                        List<HourAirQualityData> hoursAirQualityData = new ArrayList<>();

                        JsonNode hourNodes = rootNode.path("forecast").path("forecastday").get(0).path("hour");

                        for (JsonNode hour : hourNodes) {
                            JsonNode hourTimeEpoch = hour.path("time_epoch");
                            JsonNode hourTime = hour.path("time");
                            JsonNode hourDayNode = hour.path("is_day");
                            JsonNode hourConditionNode = hour.path("condition").path("text");
                            JsonNode hourAirQualityNode = hour.path("air_quality");

                            HourAirQualityData hourAirQualityData = new HourAirQualityData();
                            hourAirQualityData.setAirQuality(objectMapper.treeToValue(hourAirQualityNode, WeatherAirQuality.class));
                            hourAirQualityData.setTimeEpoch(hourTimeEpoch.asLong());
                            hourAirQualityData.setTime(hourTime.asText());
                            hourAirQualityData.setCondition(hourConditionNode.asText());
                            hourAirQualityData.setIsDay(hourDayNode.asBoolean());

                            hoursAirQualityData.add(hourAirQualityData);
                        }

                        forecastAirQualityData.setHours(hoursAirQualityData);
                    }
                }

                else {
                    List<ForecastAirQualityData> daysAirQualityData = new ArrayList<>();

                    JsonNode dayNodes = rootNode.path("forecast").path("forecastday");

                    for (JsonNode day : dayNodes) {
                        ForecastAirQualityData dayForecastAirQualityData = new ForecastAirQualityData();
                        JsonNode dayDateEpochNode = day.path("date_epoch");
                        JsonNode dayDateNode = day.path("date");
                        JsonNode dayConditionNode = day.path("day").path("condition").path("text");
                        JsonNode dayAirQualityNode = day.path("day").path("air_quality");

                        dayForecastAirQualityData.setDateEpoch(dayDateEpochNode.asLong());
                        dayForecastAirQualityData.setDate(dayDateNode.asText());
                        dayForecastAirQualityData.setCondition(dayConditionNode.asText());
                        dayForecastAirQualityData.setAirQuality(objectMapper.treeToValue(dayAirQualityNode, WeatherAirQuality.class));

                        if (includeHours) {
                            List<HourAirQualityData> hoursAirQualityData = new ArrayList<>();

                            JsonNode hourNodes = day.path("hour");

                            for (JsonNode hour : hourNodes) {
                                JsonNode hourTimeEpoch = hour.path("time_epoch");
                                JsonNode hourTime = hour.path("time");
                                JsonNode hourDayNode = hour.path("is_day");
                                JsonNode hourConditionNode = hour.path("condition").path("text");
                                JsonNode hourAirQualityNode = hour.path("air_quality");

                                HourAirQualityData hourAirQualityData = new HourAirQualityData();
                                hourAirQualityData.setAirQuality(objectMapper.treeToValue(hourAirQualityNode, WeatherAirQuality.class));
                                hourAirQualityData.setTimeEpoch(hourTimeEpoch.asLong());
                                hourAirQualityData.setTime(hourTime.asText());
                                hourAirQualityData.setCondition(hourConditionNode.asText());
                                hourAirQualityData.setIsDay(hourDayNode.asBoolean());

                                hoursAirQualityData.add(hourAirQualityData);
                            }

                            dayForecastAirQualityData.setHours(hoursAirQualityData);
                        }

                        daysAirQualityData.add(dayForecastAirQualityData);
                    }

                    forecastAirQualityData.setDays(daysAirQualityData);
                }

                if (includeCurrent) {
                    JsonNode currentAirQualityNode = rootNode.path("current").path("air_quality");
                    JsonNode currentLastUpdatedEpochNode = rootNode.path("current").path("last_updated_epoch");
                    JsonNode currentLastUpdatedNode = rootNode.path("current").path("last_updated");
                    JsonNode currentConditionNode = rootNode.path("current").path("condition").path("text");
                    JsonNode currentIsDayNode = rootNode.path("current").path("is_day");

                    CurrentAirQualityData currentAirQualityData = new CurrentAirQualityData();
                    currentAirQualityData.setAirQuality(objectMapper.treeToValue(currentAirQualityNode, WeatherAirQuality.class));
                    currentAirQualityData.setCondition(currentConditionNode.asText());
                    currentAirQualityData.setLastUpdatedEpoch(currentLastUpdatedEpochNode.asLong());
                    currentAirQualityData.setLastUpdated(currentLastUpdatedNode.asText());
                    currentAirQualityData.setIsDay(currentIsDayNode.asBoolean());

                    forecastAirQualityData.setCurrentAirQualityData(currentAirQualityData);
                }

                return forecastAirQualityData;
            }

            // Throw an exception if the API request was not successful
            throw new AirQualityServiceException("Failed to retrieve air quality data");
        }
        catch (JsonProcessingException e) {
            throw new AirQualityServiceException("Error parsing JSON response from OpenAQ API", e);
        }
        catch (RestClientException e) {
            throw new AirQualityServiceException("Error making API request to OpenAQ API", e);
        }
    }
}
