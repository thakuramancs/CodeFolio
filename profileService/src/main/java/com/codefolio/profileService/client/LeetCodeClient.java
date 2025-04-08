package com.codefolio.profileService.client;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LeetCodeClient {
    private static final String LEETCODE_API_URL = "https://leetcode.com/graphql";
    private final RestTemplate restTemplate;

    public LeetCodeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JSONObject getUserProfile(String username) {
        String query = """
            {
              matchedUser(username: "%s") {
                username
                submitStats {
                  acSubmissionNum {
                    difficulty
                    count
                  }
                }
                problemsSolvedBeatsStats {
                  difficulty
                  percentage
                }
                submitStatsGlobal {
                  acSubmissionNum {
                    difficulty
                    count
                  }
                }
                profile {
                  ranking
                  reputation
                  starRating
                }
              }
            }
            """.formatted(username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "Mozilla/5.0");

        JSONObject requestBody = new JSONObject();
        requestBody.put("query", query);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        
        try {
            String response = restTemplate.postForObject(LEETCODE_API_URL, request, String.class);
            return new JSONObject(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch LeetCode profile: " + e.getMessage(), e);
        }
    }
} 