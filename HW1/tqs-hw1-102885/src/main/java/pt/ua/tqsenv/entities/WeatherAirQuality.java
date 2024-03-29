package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAirQuality {
    private Double co;
    private Double no2;
    private Double o3;
    private Double so2;
    @JsonProperty("pm2_5")
    private Double pm25;
    private Double pm10;
    @JsonProperty("us-epa-index")
    private Integer usEpaIndex;
    @JsonProperty("gb-defra-index")
    private Integer gbDefraIndex;
}