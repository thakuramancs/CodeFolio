package com.codefolio.contestService.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CodeChefContest {
    @JsonProperty("contest_code")
    private String code;
    @JsonProperty("contest_name")
    private String name;
    @JsonProperty("contest_start_date_iso")
    private String startDate;
    @JsonProperty("contest_end_date_iso")
    private String endDate;
} 