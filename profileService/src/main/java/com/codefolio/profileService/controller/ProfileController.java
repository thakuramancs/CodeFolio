package com.codefolio.profileService.controller;

import com.codefolio.profileService.model.Profile;
import com.codefolio.profileService.service.ProfileService;
import com.codefolio.profileService.dto.AggregateStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import com.codefolio.profileService.client.LeetCodeClient;

@RestController
@RequestMapping("/profiles")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private final ProfileService profileService;
    private final LeetCodeClient leetCodeClient;

    public ProfileController(ProfileService profileService, LeetCodeClient leetCodeClient) {
        this.profileService = profileService;
        this.leetCodeClient = leetCodeClient;
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(
            @RequestParam String userId,
            @RequestParam String email,
            @RequestParam String name) {
        return ResponseEntity.ok(profileService.createProfile(userId, email, name));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Profile> updateProfile(
            @PathVariable String userId,
            @RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.updateProfile(userId, profile));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String userId) {
        profileService.deleteProfile(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/stats")
    public ResponseEntity<AggregateStats> getMyAggregateStats(@RequestParam String userId) {
        return ResponseEntity.ok(profileService.getAggregateStats(userId));
    }

    @GetMapping("/leetcode/{username}")
    public ResponseEntity<?> getLeetCodeProfile(@PathVariable String username) {
        try {
            Map<String, Object> profile = profileService.getLeetCodeProfile(username);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            log.error("Error fetching LeetCode profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error fetching LeetCode profile: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/leetcode")
    public ResponseEntity<Profile> updateLeetCodeProfile(
            @PathVariable String userId,
            @RequestParam String username) {
        return ResponseEntity.ok(profileService.updateLeetCodeProfile(userId, username));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile(
            @RequestParam String userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name) {
        try {
            String decodedUserId = URLDecoder.decode(userId, StandardCharsets.UTF_8.toString());
            log.info("Fetching profile for current user: {}", decodedUserId);
            try {
                Profile profile = profileService.getProfile(decodedUserId);
                return ResponseEntity.ok(profile);
            } catch (IllegalStateException e) {
                // Profile doesn't exist, create it if email and name are provided
                if (email != null && name != null) {
                    log.info("Creating new profile for user: {}", decodedUserId);
                    Profile newProfile = profileService.createProfile(decodedUserId, email, name);
                    return ResponseEntity.status(HttpStatus.CREATED).body(newProfile);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                      .body("Profile not found for user: " + userId + ". Provide email and name to create one.");
                }
            }
        } catch (Exception e) {
            log.error("Error processing profile for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error processing profile: " + e.getMessage());
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

            // Update LeetCode stats before returning
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
            log.error("Error fetching LeetCode stats for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error fetching LeetCode stats: " + e.getMessage());
        }
    }
} 