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
import pt.ua.tqsenv.domain.AirQualityData;
import pt.ua.tqsenv.domain.WeatherAirQuality;
import pt.ua.tqsenv.domain.WeatherLocation;
import pt.ua.tqsenv.exceptions.AirQualityServiceException;

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

    public AirQualityData getAirQualityData(String location) throws AirQualityServiceException {
        AirQualityData cachedData = cache.get(location);

        if (cachedData != null) {
            return cachedData;
        }

        AirQualityData fetchedData = fetchAirQualityData(location);
        cache.put(location, fetchedData);

        return fetchedData;
    }

    private AirQualityData fetchAirQualityData(String location) throws AirQualityServiceException {
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
                JsonNode isDayNode = rootNode.path("current").path("is_day");

                // Create a new AirQualityData object with the extracted data
                AirQualityData airQualityData = new AirQualityData();
                airQualityData.setLocation(objectMapper.treeToValue(locationNode, WeatherLocation.class));
                airQualityData.setCurrentAirQuality(objectMapper.treeToValue(currentAirQualityNode, WeatherAirQuality.class));
                airQualityData.setLastUpdatedEpoch(lastUpdatedEpochNode.asLong());
                airQualityData.setLastUpdated(lastUpdatedNode.asText());
                airQualityData.setIsDay(isDayNode.asInt());

                return airQualityData;
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
