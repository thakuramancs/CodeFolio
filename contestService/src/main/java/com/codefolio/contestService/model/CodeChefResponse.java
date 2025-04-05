package com.codefolio.contestService.model;

import lombok.Data;
import java.util.List;

@Data
public class CodeChefResponse {
    private String status;
    private String message;
    private List<CodeChefContest> presentContests;
    private List<CodeChefContest> futureContests;
} 