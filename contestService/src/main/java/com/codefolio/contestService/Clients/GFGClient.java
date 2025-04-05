package com.codefolio.contestService.Clients;

import com.codefolio.contestService.model.GFGContest;
import com.codefolio.contestService.model.GFGResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class GFGClient {
    private static final Logger logger = LoggerFactory.getLogger(GFGClient.class);
    private static final String GFG_CONTESTS_URL = "https://practice.geeksforgeeks.org/events";
    
    public GFGResponse getActiveContests() {
        logger.info("Fetching active contests from GeeksforGeeks");
        GFGResponse response = new GFGResponse();
        List<GFGContest> contests = new ArrayList<>();
        
        try {
            logger.debug("Connecting to GeeksforGeeks contests page");
            Document doc = Jsoup.connect(GFG_CONTESTS_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            Elements contestCards = doc.select("div.card");
            logger.debug("Found {} contest cards", contestCards.size());
            
            for (Element card : contestCards) {
                Element titleElement = card.selectFirst("h4.card-title");
                Element timeElement = card.selectFirst("div.card-text");
                Element linkElement = card.selectFirst("a.btn");
                
                if (titleElement != null) {
                    String title = titleElement.text().trim();
                    String description = timeElement != null ? timeElement.text().trim() : "";
                    String url = linkElement != null ? linkElement.attr("href") : "";
                    
                    if (!url.startsWith("http")) {
                        url = "https://practice.geeksforgeeks.org" + url;
                    }
                    
                    long currentTime = System.currentTimeMillis();
                    long duration = TimeUnit.DAYS.toMillis(30); // Default duration
                    
                    GFGContest contest = new GFGContest(
                        Math.abs(title.hashCode()),  // id
                        title,                       // name
                        "GeeksforGeeks",            // platform
                        currentTime,                // startTime
                        duration,                   // duration
                        url,                        // url
                        description,                // description
                        "ACTIVE"                    // status
                    );
                    
                    contests.add(contest);
                    logger.debug("Added contest: {}", title);
                }
            }
            
            response.setActiveContests(contests);
            response.setStatus("SUCCESS");
            logger.info("Successfully fetched {} contests", contests.size());
            
        } catch (IOException e) {
            logger.error("Failed to fetch contests from GeeksforGeeks: {}", e.getMessage(), e);
            response.setStatus("ERROR");
            response.setMessage("Failed to fetch contests: " + e.getMessage());
        }
        
        return response;
    }
}