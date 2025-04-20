package com.codefolio.profileService.client;

import com.codefolio.profileService.dto.PlatformStatsDTO;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Component
public class LeetCodeClient {
    private static final String LEETCODE_API_URL = "https://leetcode.com/graphql";
    private static final Logger log = LoggerFactory.getLogger(LeetCodeClient.class);
    private final RestTemplate restTemplate;

    public LeetCodeClient() {
        this.restTemplate = new RestTemplate();
    }

    public PlatformStatsDTO getUserProfile(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LeetCode username cannot be empty");
        }

        try {
            // Query user profile data with all required fields
            String profileQuery = String.format(
                "{\"query\":\"query { matchedUser(username: \\\"%s\\\") { " +
                "submitStats { acSubmissionNum { difficulty count } } " +
                "problemsSolvedBeatsStats { difficulty percentage } " +
                "profile { ranking } " +
                "tagProblemCounts { advanced { tagName problemsSolved } } " +
                "userCalendar { activeYears submissionCalendar } " +
                "badges { id name icon } " +
                "} " +
                "userContestRanking(username: \\\"%s\\\") { " +
                "attendedContestsCount " +
                "rating " +
                "globalRanking " +
                "topPercentage " +
                "badge { name } " +
                "} }\"}",
                username, username
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Referer", "https://leetcode.com");
            headers.set("Origin", "https://leetcode.com");
            
            HttpEntity<String> request = new HttpEntity<>(profileQuery, headers);
            
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(LEETCODE_API_URL, request, String.class);

                if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                    log.error("LeetCode API returned unsuccessful response: {}", response.getStatusCode());
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                        "LeetCode service is currently unavailable. Please try again later.");
                }

                JSONObject jsonResponse = new JSONObject(response.getBody());
                if (jsonResponse.has("errors")) {
                    JSONArray errors = jsonResponse.getJSONArray("errors");
                    StringBuilder errorMsg = new StringBuilder();
                    for (int i = 0; i < errors.length(); i++) {
                        JSONObject error = errors.getJSONObject(i);
                        errorMsg.append(error.getString("message")).append("; ");
                    }
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg.toString());
                }

                JSONObject data = jsonResponse.getJSONObject("data");
                JSONObject userData = data.getJSONObject("matchedUser");
                
                if (userData == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "LeetCode profile not found for username: " + username);
                }
                
                JSONObject contestData = data.optJSONObject("userContestRanking");

                PlatformStatsDTO stats = new PlatformStatsDTO();
                
                // Parse submission stats
                JSONArray acSubmissions = userData.getJSONObject("submitStats")
                    .getJSONArray("acSubmissionNum");
                
                Map<String, Integer> difficultyWise = new HashMap<>();
                int totalSolved = 0;
                
                for (int i = 0; i < acSubmissions.length(); i++) {
                    JSONObject submission = acSubmissions.getJSONObject(i);
                    String difficulty = submission.getString("difficulty").toLowerCase();
                    int count = submission.getInt("count");
                    if (!difficulty.equals("all")) {
                        difficultyWise.put(difficulty, count);
                    } else {
                        totalSolved = count;
                    }
                }
                
                stats.setTotalQuestions(totalSolved);
                stats.setDifficultyWiseSolved(difficultyWise);

                // Parse topic-wise solved problems
                Map<String, Integer> topicWise = new HashMap<>();
                if (userData.has("tagProblemCounts")) {
                    JSONArray tagProblems = userData.getJSONObject("tagProblemCounts")
                        .getJSONArray("advanced");
                    for (int i = 0; i < tagProblems.length(); i++) {
                        JSONObject tag = tagProblems.getJSONObject(i);
                        String tagName = tag.getString("tagName");
                        int solved = tag.getInt("problemsSolved");
                        if (solved > 0) {
                            topicWise.put(tagName, solved);
                        }
                    }
                }
                stats.setTopicWiseSolved(topicWise);

                // Parse contest stats
                if (contestData != null) {
                    stats.setRating(contestData.optInt("rating", 0));
                    stats.setContestRanking(contestData.optInt("globalRanking", 0));
                    stats.setTotalContests(contestData.optInt("attendedContestsCount", 0));
                } else {
                    JSONObject profile = userData.getJSONObject("profile");
                    stats.setContestRanking(profile.optInt("ranking", 0));
                    stats.setRating(0);
                    stats.setTotalContests(0);
                }

                // Parse submission calendar
                JSONObject calendar = userData.getJSONObject("userCalendar");
                String submissionCalendar = calendar.optString("submissionCalendar", "{}");
                stats.setSubmissionCalendar(submissionCalendar);
                
                // Calculate active days from calendar
                JSONObject calendarData = new JSONObject(submissionCalendar);
                stats.setTotalActiveDays(calendarData.length());

                // Parse badges
                JSONArray badges = userData.optJSONArray("badges");
                if (badges != null && badges.length() > 0) {
                    JSONArray badgesArray = new JSONArray();
                    for (int i = 0; i < badges.length(); i++) {
                        JSONObject badge = badges.getJSONObject(i);
                        JSONObject badgeObj = new JSONObject();
                        badgeObj.put("name", badge.getString("name"));
                        badgeObj.put("icon", badge.optString("icon", ""));
                        badgesArray.put(badgeObj);
                    }
                    stats.setAwards(badgesArray.toString());
                }

                return stats;

            } catch (ResourceAccessException e) {
                log.error("Failed to connect to LeetCode API: {}", e.getMessage());
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                    "Unable to connect to LeetCode. Please check your internet connection and try again later.");
            }

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching LeetCode profile for {}: {}", username, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to process LeetCode profile data. Please try again later.");
        }
    }

    private int parseNumber(String text) {
        try {
            return Integer.parseInt(text.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
} 