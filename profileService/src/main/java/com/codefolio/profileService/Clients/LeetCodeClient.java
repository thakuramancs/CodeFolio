package com.codefolio.profileService.Clients;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LeetCodeClient {
    private static final String LEETCODE_API = "https://leetcode.com/graphql";
    private final RestTemplate restTemplate;

    public Map<String, Object> getUserProfile(String username) {
        String query = """
            {
              matchedUser(username: "%s") {
                submitStats {
                  acSubmissionNum {
                    difficulty
                    count
                  }
                }
                contestRanking {
                  rating
                  globalRanking
                  attendedContestsCount
                }
              }
            }
            """.formatted(username);

        Map<String, String> body = new HashMap<>();
        body.put("query", query);

        try {
            JsonNode response = restTemplate.postForObject(LEETCODE_API, body, JsonNode.class);
            JsonNode user = response.get("data").get("matchedUser");
            
            Map<String, Object> stats = new HashMap<>();
            JsonNode submissions = user.get("submitStats").get("acSubmissionNum");
            
            for (JsonNode submission : submissions) {
                String difficulty = submission.get("difficulty").asText();
                int count = submission.get("count").asInt();
                stats.put(difficulty.toLowerCase() + "Solved", count);
            }
            
            JsonNode contest = user.get("contestRanking");
            if (!contest.isNull()) {
                stats.put("rating", contest.get("rating").asInt());
                stats.put("contestsJoined", contest.get("attendedContestsCount").asInt());
            }
            
            return stats;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch LeetCode profile: " + username);
        }
    }
} 