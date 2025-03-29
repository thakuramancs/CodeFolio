package com.codefolio.contestService.Controllers;

import com.codefolio.contestService.service.ContestService;
import com.codefolio.contestService.model.Contest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contests")
public class ContestController {
    private final ContestService contestService;

    @Autowired
    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Contest>> getUpcomingContests() {
        return ResponseEntity.ok(contestService.getUpcomingContests());
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<Contest>> getOngoingContests() {
        return ResponseEntity.ok(contestService.getOngoingContests());
    }
}