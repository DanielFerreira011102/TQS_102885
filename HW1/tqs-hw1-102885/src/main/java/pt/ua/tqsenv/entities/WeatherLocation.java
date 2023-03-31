package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class WeatherLocation {
    private String name;
    private String region;
    private String country;
    private Double lat;
    private Double lon;
    @JsonProperty("tz_id")
    private String tzId;
    @JsonProperty("localtime_epoch")
    private Long localtimeEpoch;
    private String localtime;
}