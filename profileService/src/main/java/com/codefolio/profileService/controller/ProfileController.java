package com.codefolio.profileService.controller;

import com.codefolio.profileService.model.Profile;
import com.codefolio.profileService.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile(
            @RequestParam String userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name) {
        try {
            String decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8.toString());
            log.info("Fetching profile for user: {}", decodedUserId);
            try {
                Profile profile = profileService.getProfile(decodedUserId);
                return ResponseEntity.ok(profile);
            } catch (IllegalStateException e) {
                if (email != null && name != null) {
                    log.info("Creating new profile for user: {}", decodedUserId);
                    Profile newProfile = profileService.createProfile(decodedUserId, email, name);
                    return ResponseEntity.status(HttpStatus.CREATED).body(newProfile);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                      .body("Profile not found. Provide email and name to create one.");
                }
            }
        } catch (Exception e) {
            log.error("Error processing profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error processing profile: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable String userId,
            @RequestBody Profile profile) {
        try {
            return ResponseEntity.ok(profileService.updateProfile(userId, profile));
        } catch (Exception e) {
            log.error("Error updating profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error updating profile: " + e.getMessage());
        }
    }

    @GetMapping("/platforms/leetcode/stats")
    public ResponseEntity<?> getLeetCodeStats(@RequestParam String userId) {
        try {
            log.info("Fetching LeetCode stats for user: {}", userId);
            Profile profile = profileService.getProfile(userId);
            
            if (profile.getLeetcodeUsername() == null || profile.getLeetcodeUsername().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body("LeetCode username not set for this user");
            }

            profile = profileService.updateLeetCodeProfile(userId, profile.getLeetcodeUsername());
            
            return ResponseEntity.ok(Map.of(
                "totalSolved", profile.getLeetcodeTotalSolved(),
                "easySolved", profile.getLeetcodeEasySolved(),
                "mediumSolved", profile.getLeetcodeMediumSolved(),
                "hardSolved", profile.getLeetcodeHardSolved(),
                "rating", profile.getLeetcodeRating(),
                "username", profile.getLeetcodeUsername()
            ));
        } catch (Exception e) {
            log.error("Error fetching LeetCode stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error fetching LeetCode stats: " + e.getMessage());
        }
    }

    @GetMapping("/platforms/codeforces/stats")
    public ResponseEntity<?> getCodeforcesStats(@RequestParam String userId) {
        try {
            log.info("Fetching CodeForces stats for user: {}", userId);
            Profile profile = profileService.getProfile(userId);
            
            if (profile.getCodeforcesUsername() == null || profile.getCodeforcesUsername().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body("CodeForces username not set for this user");
            }

            profile = profileService.updateCodeforcesProfile(userId, profile.getCodeforcesUsername());
            
            return ResponseEntity.ok(Map.of(
                "rating", profile.getCodeforcesRating(),
                "rank", profile.getCodeforcesRank(),
                "contestCount", profile.getCodeforcesContestCount(),
                "username", profile.getCodeforcesUsername()
            ));
        } catch (Exception e) {
            log.error("Error fetching CodeForces stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error fetching CodeForces stats: " + e.getMessage());
        }
    }

    @GetMapping("/platforms/codechef/stats")
    public ResponseEntity<Map<String, Object>> getCodeChefStats(@RequestParam String userId) {
        try {
            String decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8.toString());
            log.info("Fetching CodeChef stats for user: {}", decodedUserId);
            Profile profile = profileService.getProfile(decodedUserId);
            if (profile.getCodechefUsername() == null || profile.getCodechefUsername().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "CodeChef username not set for this user"));
            }

            JSONObject stats = profileService.getCodeChefProfile(decodedUserId);
            return ResponseEntity.ok(Map.of(
                "username", profile.getCodechefUsername(),
                "rating", stats.optInt("rating", 0),
                "stars", stats.optString("stars", ""),
                "globalRank", stats.optInt("globalRank", 0),
                "countryRank", stats.optInt("countryRank", 0),
                "problemsSolved", stats.optInt("problemsSolved", 0)
            ));
        } catch (Exception e) {
            log.error("Error fetching CodeChef stats: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error fetching CodeChef stats: " + e.getMessage()));
        }
    }
} 