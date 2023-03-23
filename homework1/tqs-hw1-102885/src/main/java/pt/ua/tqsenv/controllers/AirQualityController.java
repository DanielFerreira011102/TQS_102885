package pt.ua.tqsenv.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.tqsenv.domain.AirQualityData;
import pt.ua.tqsenv.exceptions.AirQualityServiceException;
import pt.ua.tqsenv.services.AirQualityService;

@RestController
@RequestMapping("/api/v1")
public class AirQualityController {

    private final AirQualityService airQualityService;

    public AirQualityController(AirQualityService airQualityService) {
        this.airQualityService = airQualityService;
    }

    @GetMapping("/current")
    public ResponseEntity<AirQualityData> getAirQualityData(@RequestParam(name = "location") String location) throws AirQualityServiceException {
        AirQualityData airQualityData = airQualityService.getAirQualityData(location);
        return airQualityData != null ? ResponseEntity.ok(airQualityData) : ResponseEntity.notFound().build();
    }
}
