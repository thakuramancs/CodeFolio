package com.codefolio.profileService.client;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.io.IOException;

@Component
public class CodeChefClient {
    private static final String CODECHEF_URL = "https://www.codechef.com/users/%s";
    private static final Logger log = LoggerFactory.getLogger(CodeChefClient.class);

    public JSONObject getUserProfile(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CodeChef username cannot be empty");
        }

        try {
            log.info("Fetching CodeChef profile for user: {}", username);
            
            Document doc = Jsoup.connect(String.format(CODECHEF_URL, username))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .timeout(30000)
                .get();

            // Check if profile exists by looking for error message or user details container
            Element errorElement = doc.selectFirst("div.error-message");
            Element userDetailsContainer = doc.selectFirst(".user-details-container");
            if (errorElement != null || userDetailsContainer == null) {
                log.warn("CodeChef profile not found for username: {}", username);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CodeChef profile not found");
            }

            // Extract rating
            Element ratingElement = doc.selectFirst(".rating-number");
            String rating = "0";
            if (ratingElement != null) {
                rating = ratingElement.text().replaceAll("[^0-9]", "");
                if (rating.isEmpty()) rating = "0";
            }
            
            // Extract stars from username section
            String stars = "0★";
            Element starsElement = doc.selectFirst(".user-details .rating");
            if (starsElement != null) {
                String starsText = starsElement.text().trim();
                if (!starsText.isEmpty()) {
                    stars = starsText;
                }
            }
            
            // Extract global and country ranks
            String globalRank = "0";
            String countryRank = "0";
            Element rankingSection = doc.selectFirst(".rating-ranks");
            if (rankingSection != null) {
                Elements rankItems = rankingSection.select("ul.inline-list li");
                for (Element item : rankItems) {
                    String rankText = item.text().trim();
                    Element rankValue = item.selectFirst("strong");
                    if (rankValue != null) {
                        String rank = rankValue.text().replaceAll("[^0-9]", "");
                        if (rank.isEmpty()) rank = "0";
                        
                        if (rankText.contains("Global Rank")) {
                            globalRank = rank;
                        } else if (rankText.contains("Country Rank")) {
                            countryRank = rank;
                        }
                    }
                }
            }
            
            // Extract problems solved - look for "Total Problems Solved: X" text
            int problemsSolved = 0;
            Element problemsSection = doc.selectFirst("section.problems-solved");
            if (problemsSection != null) {
                for (Element h3 : problemsSection.select("h3")) {
                    String text = h3.text();
                    if (text.startsWith("Total Problems Solved:")) {
                        try {
                            String count = text.split(":")[1].trim();
                            problemsSolved = Integer.parseInt(count);
                            break;
                        } catch (NumberFormatException e) {
                            log.warn("Failed to parse problems solved count from text: {}", text);
                        }
                    }
                }
            }

            log.info("Successfully scraped profile data for {}: rating={}, stars={}, globalRank={}, countryRank={}, problems={}",
                    username, rating, stars, globalRank, countryRank, problemsSolved);

            return new JSONObject()
                .put("username", username)
                .put("rating", Integer.parseInt(rating))
                .put("stars", stars)
                .put("globalRank", Integer.parseInt(globalRank))
                .put("countryRank", Integer.parseInt(countryRank))
                .put("problemsSolved", problemsSolved);

        } catch (IOException e) {
            log.error("Network error while fetching CodeChef profile for {}: {}", username, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to connect to CodeChef");
        } catch (ResponseStatusException e) {
            throw e; // Re-throw ResponseStatusException
        } catch (Exception e) {
            log.error("Error scraping CodeChef profile for {}: {}", username, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to process CodeChef profile data: " + e.getMessage());
        }
    }
} 