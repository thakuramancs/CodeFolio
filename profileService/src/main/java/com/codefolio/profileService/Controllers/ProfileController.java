package com.codefolio.profileService.Controllers;

import com.codefolio.profileService.Clients.GitHubClient;
import com.codefolio.profileService.model.GitHubProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import feign.FeignException;    

@Slf4j
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final GitHubClient gitHubClient;

    public ProfileController(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @GetMapping("/github/{username}")
    public ResponseEntity<GitHubProfile> getGitHubProfile(@PathVariable String username) {
        try {
            log.info("Fetching GitHub profile for user: {}", username);
            
            GitHubProfile profile = gitHubClient.getProfile(username, "application/vnd.github.v3+json");
            
            log.info("Received profile data: {}", profile);
            log.info("Public repos count: {}", profile.getPublicRepos());
            
            if (profile.getPublicRepos() == null) {
                log.warn("Public repos count is null for user: {}", username);
            }
            
            return ResponseEntity.ok(profile);
        } catch (FeignException e) {
            log.error("Feign client error for user {}: {} - {}", 
                     username, e.status(), e.getMessage());
            return ResponseEntity.status(e.status()).build();
        } catch (Exception e) {
            log.error("Unexpected error fetching GitHub profile for {}: {}", 
                     username, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/github/debug/{username}")
    public ResponseEntity<?> debugGitHubProfile(@PathVariable String username) {
        try {
            GitHubProfile profile = gitHubClient.getProfile(username, "application/vnd.github.v3+json");
            return ResponseEntity.ok(Map.of(
                "rawProfile", profile,
                "publicRepos", profile.getPublicRepos(),
                "login", profile.getLogin(),
                "name", profile.getName()
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "error", e.getMessage(),
                "errorType", e.getClass().getSimpleName()
            ));
        }
    }
}