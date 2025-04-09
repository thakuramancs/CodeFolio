package com.codefolio.profileService.client;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class LeetCodeClient {
    private final LeetCodeFeignClient leetCodeFeignClient;

    public LeetCodeClient(LeetCodeFeignClient leetCodeFeignClient) {
        this.leetCodeFeignClient = leetCodeFeignClient;
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

        JSONObject requestBody = new JSONObject();
        requestBody.put("query", query);

        try {
            String response = leetCodeFeignClient.getUserProfile(
                "Mozilla/5.0",
                requestBody.toString()
            );
            return new JSONObject(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch LeetCode profile: " + e.getMessage(), e);
        }
    }
} 