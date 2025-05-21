package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略JSON中存在但POJO中没有的字段
public class Clan {
    private String tag;
    private String name;
    private Integer clanLevel;
    private BadgeUrls badgeUrls;
}