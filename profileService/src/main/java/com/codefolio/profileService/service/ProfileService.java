package com.codefolio.profileService.service;

import com.codefolio.profileService.model.Profile;
import com.codefolio.profileService.dto.AggregateStats;
import java.util.Map;

public interface ProfileService {
    // Basic Profile Operations
    Profile createProfile(String userId, String email, String name);
    Profile getProfile(String userId);
    Profile updateProfile(String userId, Profile profile);
    void deleteProfile(String userId);
    
    // Platform-specific profile operations
    Map<String, Object> getLeetCodeProfile(String username);
    Map<String, Object> getCodeforcesProfile(String username);
    Map<String, Object> getCodeChefProfile(String username);
    Map<String, Object> getHackerRankProfile(String username);
    Map<String, Object> getGeeksForGeeksProfile(String username);
    
    // Platform username management
    Profile updatePlatformUsername(String userId, String platform, String username);
    
    // Stats operations
    AggregateStats getAggregateStats(String userId);
} 