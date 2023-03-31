package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastAirQualityData extends AirQualityData {
    @JsonProperty("current")
    private CurrentAirQualityData currentAirQualityData;
    @JsonProperty("date_epoch")
    private Long dateEpoch;
    @JsonProperty("date")
    private String date;
    private List<ForecastAirQualityData> days;
    private List<HourAirQualityData> hours;
}
