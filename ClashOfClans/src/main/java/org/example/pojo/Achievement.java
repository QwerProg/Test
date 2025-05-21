package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Achievement {
    private String name;
    private Integer stars;
    private Integer value;
    private Integer target;
    private String info;
    private String completionInfo;
    private String village;
}