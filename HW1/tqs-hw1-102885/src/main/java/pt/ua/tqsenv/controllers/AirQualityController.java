package pt.ua.tqsenv.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.tqsenv.entities.AirQualityData;
import pt.ua.tqsenv.entities.CacheAnalyticsData;
import pt.ua.tqsenv.entities.CurrentAirQualityData;
import pt.ua.tqsenv.entities.ForecastAirQualityData;
import pt.ua.tqsenv.services.AirQualityService;

@RestController
@RequestMapping("/api/v1")
public class AirQualityController {

    Logger logger = LoggerFactory.getLogger(AirQualityController.class);
    private final AirQualityService airQualityService;

    public AirQualityController(AirQualityService airQualityService) {
        this.airQualityService = airQualityService;
    }

    @GetMapping("/current")
    public ResponseEntity<AirQualityData> getCurrentAirQualityData(@RequestParam(name = "location") String location) {

        logger.info("Received request for current air quality data for location: {}", location);

        CurrentAirQualityData currentAirQualityData = airQualityService.getCurrentAirQualityData(location);
        if (currentAirQualityData != null) {
            logger.info("Returning current air quality data for location: {}", location);
            return ResponseEntity.ok(currentAirQualityData);
        } else {
            logger.warn("No current air quality data found for location: {}", location);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/forecast")
    public ResponseEntity<AirQualityData> getForecastAirQualityData(@RequestParam(name = "location") String location,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "current", defaultValue = "false") boolean current,
            @RequestParam(name = "days", defaultValue = "1") int days,
            @RequestParam(name = "hours", defaultValue = "false") boolean hours) {

        logger.info("Received request for forecast air quality data for location {} with date {}, current={}, days={}, hours={}",
                location, date, current, days, hours);

        ForecastAirQualityData forecastAirQualityData = airQualityService.getForecastAirQualityData(location, date, current, days, hours);

        if (forecastAirQualityData != null) {
            logger.info("Returning forecast air quality data for location {} with date {}, current={}, days={}, hours={}",
                    location, date, current, days, hours);
            return ResponseEntity.ok(forecastAirQualityData);
        } else {
            logger.warn("No forecast air quality data found for location {} with date {}, current={}, days={}, hours={}",
                    location, date, current, days, hours);
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/cache")
    public ResponseEntity<CacheAnalyticsData> getForecastCacheAnalyticsData() {
        logger.info("Received request for cache analytics data");
        CacheAnalyticsData cacheAnalyticsData = airQualityService.getCacheAnalyticsData();
        if (cacheAnalyticsData != null) {
            logger.info("Returning cache analytics data");
            return ResponseEntity.ok(cacheAnalyticsData);
        } else {
            logger.warn("No cache analytics data found");
            return ResponseEntity.notFound().build();
        }
    }
}
