package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// 与 Equipment.java 结构相同，但为了区分顶级列表和英雄内嵌列表，可以单独命名
// 或者直接复用 Equipment.java 也可以，这里为清晰起见单独命名
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroEquipmentItem { // 代表顶级 heroEquipment 数组中的一项
    private String name;
    private Integer level;
    private Integer maxLevel;
    private String village;
}