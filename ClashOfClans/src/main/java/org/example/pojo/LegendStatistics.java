package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegendStatistics {
    private Integer legendTrophies;
    private SeasonStats previousSeason;
    private SeasonStats bestSeason;
    private SeasonStats currentSeason;
}