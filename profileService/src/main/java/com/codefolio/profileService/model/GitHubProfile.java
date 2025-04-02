package com.codefolio.profileService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubProfile {
    private String login;
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String bio;
    @JsonProperty("public_repos")
    private Integer publicRepos;
    private Integer followers = 0;
    private Integer following = 0;
    @JsonProperty("html_url")
    private String htmlUrl;

    // Custom getter to ensure we never return null
    public Integer getPublicRepos() {
        return publicRepos != null ? publicRepos : 0;
    }
} 