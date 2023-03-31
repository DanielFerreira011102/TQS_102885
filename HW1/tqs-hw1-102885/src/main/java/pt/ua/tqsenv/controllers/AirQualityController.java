package pt.ua.tqsenv.controllers;

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

    private final AirQualityService airQualityService;

    public AirQualityController(AirQualityService airQualityService) {
        this.airQualityService = airQualityService;
    }

    @GetMapping("/current")
    public ResponseEntity<AirQualityData> getCurrentAirQualityData(@RequestParam(name = "location") String location) {
        CurrentAirQualityData currentAirQualityData = airQualityService.getCurrentAirQualityData(location);
        return currentAirQualityData != null ? ResponseEntity.ok(currentAirQualityData) : ResponseEntity.notFound().build();
    }

    @GetMapping("/forecast")
    public ResponseEntity<AirQualityData> getForecastAirQualityData(@RequestParam(name = "location") String location, @RequestParam(name = "date", required = false) String date, @RequestParam(name = "current", defaultValue = "false") Boolean current, @RequestParam(name = "days", defaultValue = "1") Integer days, @RequestParam(name = "hours", defaultValue = "false") Boolean hours) {
        ForecastAirQualityData forecastAirQualityData = airQualityService.getForecastAirQualityData(location, date, current, days, hours);
        return forecastAirQualityData != null ? ResponseEntity.ok(forecastAirQualityData) : ResponseEntity.notFound().build();
    }

    @GetMapping("/cache")
    public ResponseEntity<CacheAnalyticsData> getForecastCacheAnalyticsData() {
        CacheAnalyticsData cacheAnalyticsData = airQualityService.getCacheAnalyticsData();
        return cacheAnalyticsData != null ? ResponseEntity.ok(cacheAnalyticsData) : ResponseEntity.notFound().build();
    }
}
