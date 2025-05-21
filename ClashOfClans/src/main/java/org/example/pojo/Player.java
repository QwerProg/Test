package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略JSON中存在但POJO中没有的字段
public class Player {
    private String tag;
    private String name;
    private Integer townHallLevel;
    private Integer townHallWeaponLevel;
    private Integer expLevel;
    private Integer trophies;
    private Integer bestTrophies;
    private Integer warStars;
    private Integer attackWins;
    private Integer defenseWins;
    private Integer builderHallLevel;
    // JSON中是 builderBaseTrophies, 映射到 versusTrophies (或保持一致)
    @JsonProperty("builderBaseTrophies")
    private Integer versusTrophies;
    @JsonProperty("bestBuilderBaseTrophies")
    private Integer bestVersusTrophies;
    private String role;
    private String warPreference;
    private Integer donations;
    private Integer donationsReceived;
    private Integer clanCapitalContributions;

    private Clan clan;
    private League league;
    private BuilderBaseLeague builderBaseLeague;
    private LegendStatistics legendStatistics;

    private List<Achievement> achievements;
    private List<Label> labels;
    private List<Troop> troops;
    private List<Hero> heroes;
    private List<Spell> spells;
    private List<HeroEquipmentItem> heroEquipment; // 顶级英雄装备列表

    // 其他字段根据需要添加，例如 playerHouse 等
}