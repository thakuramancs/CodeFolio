package com.codefolio.profileService.controller;

import com.codefolio.profileService.model.Profile;
import com.codefolio.profileService.service.ProfileService;
import com.codefolio.profileService.dto.AggregateStats;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<Profile> createProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String email,
            @RequestParam String name) {
        return ResponseEntity.ok(profileService.createProfile(jwt.getSubject(), email, name));
    }

    @GetMapping("/me")
    public ResponseEntity<Profile> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(profileService.getProfile(jwt.getSubject()));
    }

    @PutMapping("/me")
    public ResponseEntity<Profile> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.updateProfile(jwt.getSubject(), profile));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(@AuthenticationPrincipal Jwt jwt) {
        profileService.deleteProfile(jwt.getSubject());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/platforms/{platform}")
    public ResponseEntity<Profile> updatePlatformUsername(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String platform,
            @RequestParam String username) {
        return ResponseEntity.ok(profileService.updatePlatformUsername(jwt.getSubject(), platform, username));
    }

    @GetMapping("/me/stats")
    public ResponseEntity<AggregateStats> getMyAggregateStats(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(profileService.getAggregateStats(jwt.getSubject()));
    }
} 