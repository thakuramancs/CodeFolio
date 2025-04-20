package com.codefolio.profileService.client;

import com.codefolio.profileService.dto.PlatformStatsDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

@Component
public class CodeforcesClient {
    private static final String CF_API_USER_INFO = "https://codeforces.com/api/user.info?handles=%s";
    private static final String CF_API_USER_STATUS = "https://codeforces.com/api/user.status?handle=%s";
    private static final String CF_API_USER_RATING = "https://codeforces.com/api/user.rating?handle=%s";
    private static final Logger log = LoggerFactory.getLogger(CodeforcesClient.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public PlatformStatsDTO getUserProfile(String handle) {
        if (handle == null || handle.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codeforces handle cannot be empty");
        }

        try {
            String encodedHandle = java.net.URLEncoder.encode(handle.trim(), "UTF-8");
            
            // Get user info, submissions and rating history
            JSONObject userInfo = new JSONObject(restTemplate.getForObject(
                String.format(CF_API_USER_INFO, encodedHandle), String.class));
            JSONObject userStatus = new JSONObject(restTemplate.getForObject(
                String.format(CF_API_USER_STATUS, encodedHandle), String.class));
            JSONObject userRating = new JSONObject(restTemplate.getForObject(
                String.format(CF_API_USER_RATING, encodedHandle), String.class));

            if (!"OK".equals(userInfo.getString("status"))) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Codeforces profile not found");
            }

            // Process the data
            PlatformStatsDTO stats = new PlatformStatsDTO();
            JSONObject user = userInfo.getJSONArray("result").getJSONObject(0);
            
            // Set basic info
            stats.setRating(user.optInt("rating", 0));
            stats.setContestRanking(user.optInt("maxRank", 0));
            
            // Process submissions
            if ("OK".equals(userStatus.getString("status"))) {
                JSONArray submissions = userStatus.getJSONArray("result");
                Map<String, Integer> difficultyMap = new HashMap<>();
                Map<String, Integer> topicMap = new HashMap<>();
                Map<String, Integer> submissionDates = new HashMap<>();
                
                int totalSolved = 0;
                for (int i = 0; i < submissions.length(); i++) {
                    JSONObject submission = submissions.getJSONObject(i);
                    if ("OK".equals(submission.getString("verdict"))) {
                        totalSolved++;
                        
                        // Track difficulty and topics
                        JSONObject problem = submission.getJSONObject("problem");
                        String difficulty = String.valueOf(problem.optInt("rating", 0));
                        difficultyMap.merge(difficulty, 1, Integer::sum);
                        
                        JSONArray tags = problem.getJSONArray("tags");
                        for (int j = 0; j < tags.length(); j++) {
                            topicMap.merge(tags.getString(j), 1, Integer::sum);
                        }
                        
                        // Track submission date
                        String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                            .format(new java.util.Date(submission.getLong("creationTimeSeconds") * 1000));
                        submissionDates.merge(date, 1, Integer::sum);
                    }
                }
                
                stats.setTotalQuestions(totalSolved);
                stats.setDifficultyWiseSolved(difficultyMap);
                stats.setTopicWiseSolved(topicMap);
                stats.setSubmissionCalendar(new JSONObject(submissionDates).toString());
                stats.setTotalActiveDays(submissionDates.size());
            }

            // Process contest participation
            if ("OK".equals(userRating.getString("status"))) {
                JSONArray contests = userRating.getJSONArray("result");
                stats.setTotalContests(contests.length());
            }

            // Create awards array
            JSONArray awards = new JSONArray();
            if (user.has("rank")) {
                JSONObject rankAward = new JSONObject();
                rankAward.put("name", "Current Rank");
                rankAward.put("value", user.getString("rank"));
                awards.put(rankAward);
            }
            if (user.has("maxRank")) {
                JSONObject maxRankAward = new JSONObject();
                maxRankAward.put("name", "Max Rank");
                maxRankAward.put("value", user.getString("maxRank"));
                awards.put(maxRankAward);
            }
            stats.setAwards(awards.toString());

            return stats;

        } catch (Exception e) {
            log.error("Error fetching Codeforces profile for {}: {}", handle, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to process Codeforces profile data: " + e.getMessage());
        }
    }
} 