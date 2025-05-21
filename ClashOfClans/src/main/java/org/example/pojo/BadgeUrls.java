package org.example.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadgeUrls {
    private String small;
    private String large;
    private String medium;
}