package com.codefolio.contestService.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.codefolio.contestService.model.CodeChefResponse;

@FeignClient(name = "codechef-api", url = "https://www.codechef.com")
public interface CodeChefClient {
    @GetMapping("/api/list/contests/all")
    CodeChefResponse getContests();
} 