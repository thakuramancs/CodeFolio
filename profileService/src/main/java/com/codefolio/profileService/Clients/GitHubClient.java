package com.codefolio.profileService.Clients;
// Filename: GitHubClient.java
import com.codefolio.profileService.model.GitHubProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.codefolio.profileService.config.GitHubClientConfig;

@FeignClient(
    name = "github-api", 
    url = "${github.api.url:https://api.github.com}",
    configuration = GitHubClientConfig.class
)
public interface GitHubClient {
    
    @GetMapping(value = "/users/{username}", produces = "application/json")
    GitHubProfile getProfile(
        @PathVariable("username") String username,
        @RequestHeader("Accept") String accept
    );
}