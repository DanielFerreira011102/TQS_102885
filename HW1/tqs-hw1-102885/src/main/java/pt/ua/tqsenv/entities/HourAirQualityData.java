package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HourAirQualityData extends AirQualityData {
    @JsonProperty("time_epoch")
    private Long timeEpoch;
    @JsonProperty("time")
    private String time;
}
