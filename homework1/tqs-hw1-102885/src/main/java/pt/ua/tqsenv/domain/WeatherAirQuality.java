package pt.ua.tqsenv.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAirQuality {
    private double co;
    private double no2;
    private double o3;
    private double so2;
    private double pm2_5;
    private double pm10;
    @JsonProperty("us-epa-index")
    private int usEpaIndex;
    @JsonProperty("gb-defra-index")
    private int gbDefraIndex;
}