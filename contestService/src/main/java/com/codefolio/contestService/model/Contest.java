package com.codefolio.contestService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    private int id;
    private String name;
    private String platform;
    private long startTime;
    private long duration;
    private String url;
}