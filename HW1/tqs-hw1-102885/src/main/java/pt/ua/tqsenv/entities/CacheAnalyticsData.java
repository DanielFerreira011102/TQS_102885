package pt.ua.tqsenv.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheAnalyticsData {
    private String date;
    @JsonProperty("request_count")
    private Integer requestsCount;
    @JsonProperty("cache_hits")
    private Integer cacheHits;
    @JsonProperty("cache_misses")
    private Integer cacheMisses;
    @JsonProperty("expired_count")
    private Integer expiredCount;
}
