package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentAirQualityData extends AirQualityData {
    @JsonProperty("last_updated_epoch")
    private Long lastUpdatedEpoch;
    @JsonProperty("last_updated")
    private String lastUpdated;
}