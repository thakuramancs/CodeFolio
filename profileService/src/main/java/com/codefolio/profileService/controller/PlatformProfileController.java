package com.codefolio.profileService.controller;

import com.codefolio.profileService.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/platform-profiles")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class PlatformProfileController {

    private final ProfileService profileService;

    @GetMapping("/leetcode/{username}")
    public ResponseEntity<Map<String, Object>> getLeetCodeProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getLeetCodeProfile(username));
    }

    @GetMapping("/codeforces/{username}")
    public ResponseEntity<Map<String, Object>> getCodeforcesProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getCodeforcesProfile(username));
    }

    @GetMapping("/codechef/{username}")
    public ResponseEntity<Map<String, Object>> getCodeChefProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getCodeChefProfile(username));
    }

    @GetMapping("/hackerrank/{username}")
    public ResponseEntity<Map<String, Object>> getHackerRankProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getHackerRankProfile(username));
    }

    @GetMapping("/geeksforgeeks/{username}")
    public ResponseEntity<Map<String, Object>> getGeeksForGeeksProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getGeeksForGeeksProfile(username));
    }
} 