package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
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
