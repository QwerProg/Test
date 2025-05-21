package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Troop {
    private String name;
    private Integer level;
    private Integer maxLevel;
    private String village;
    @JsonProperty("superTroopIsActive") // 处理JSON中首字母小写，后续大写的特殊情况
    private boolean superTroopIsActive;
}