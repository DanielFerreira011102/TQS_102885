package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirQualityData {
    private WeatherLocation location;
    @JsonProperty("air_quality")
    private WeatherAirQuality airQuality;
    private String condition;
    @JsonProperty("is_day")
    private Boolean isDay;
}
