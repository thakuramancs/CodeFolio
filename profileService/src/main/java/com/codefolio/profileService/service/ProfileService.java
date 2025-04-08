package com.codefolio.profileService.service;

import com.codefolio.profileService.model.Profile;
import com.codefolio.profileService.dto.AggregateStats;
import java.util.Map;

public interface ProfileService {
    Profile createProfile(String userId, String email, String name);
    Profile getProfile(String userId);
    Profile updateProfile(String userId, Profile profile);
    void deleteProfile(String userId);
    Map<String, Object> getLeetCodeProfile(String username);
    AggregateStats getAggregateStats(String userId);
    Profile updateLeetCodeProfile(String userId, String username);
} 