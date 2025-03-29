package com.codefolio.contestService.model;

import lombok.Data;

@Data
public class Contest {
    private Long id;
    private String name;
    private String type;
    private String phase;
    private Boolean frozen;
    private Long durationSeconds;
    private Long startTimeSeconds;
    private Long relativeTimeSeconds;
    private String preparedBy;
    private String websiteUrl;
    private String description;
    private Integer difficulty;
    private String kind;
    private String icpcRegion;
    private String country;
    private String city;
    private String season;
} 