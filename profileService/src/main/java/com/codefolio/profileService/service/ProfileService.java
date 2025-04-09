package com.codefolio.profileService.service;

import com.codefolio.profileService.model.Profile;
import java.util.Map;
import org.json.JSONObject;

public interface ProfileService {
    Profile createProfile(String userId, String email, String name);
    Profile getProfile(String userId);
    Profile updateProfile(String userId, Profile profile);
    void deleteProfile(String userId);
    Map<String, Object> getLeetCodeProfile(String username);
    Profile updateLeetCodeProfile(String userId, String username);
    Map<String, Object> getCodeforcesProfile(String handle);
    Profile updateCodeforcesProfile(String userId, String handle);
    JSONObject getCodeChefProfile(String userId);
} 