package com.codefolio.profileService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "leetcode-client", url = "https://leetcode.com")
public interface LeetCodeFeignClient {
    
    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE)
    String getUserProfile(
        @RequestHeader("User-Agent") String userAgent,
        @RequestBody String query
    );
} 