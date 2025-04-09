package com.codefolio.profileService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "codeforces", url = "https://codeforces.com/api")
public interface CodeforcesClient {
    
    @GetMapping("/user.info?handles={handle}")
    String getUserInfo(@PathVariable("handle") String handle);
    
    @GetMapping("/user.rating?handle={handle}")
    String getUserRating(@PathVariable("handle") String handle);
} 