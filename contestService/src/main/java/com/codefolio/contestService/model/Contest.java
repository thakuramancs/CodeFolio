package com.codefolio.contestService.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contest {
    private int id;
    private String name;
    private String platform;
    private long startTime;
    private long duration;
    private String url;

    public Contest(int id, String name, String platform, long startTime, long duration, String url) {
        this.id = id;
        this.name = name;
        this.platform = platform;
        this.startTime = startTime;
        this.duration = duration;
        this.url = url;
    }
}