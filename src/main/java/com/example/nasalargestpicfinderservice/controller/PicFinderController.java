package com.example.nasalargestpicfinderservice.controller;

import com.example.nasalargestpicfinderservice.service.LargestPicFinderService;
import com.example.nasalargestpicfinderservice.service.dto.Photos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class PicFinderController {

    public static final String API_KEY = "DEMO_KEY";
    public static final String BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";

    @Autowired
    private LargestPicFinderService service;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/pictures/{sol}/largest")
    @Cacheable("pics")
    public ResponseEntity findLargestPicAndRedirect(@PathVariable(required = true) Integer sol) {
        ResponseEntity<Photos> responseEntity = restTemplate.getForEntity(createUri(BASE_URL, params(sol)), Photos.class);
        log.info("response status code is {}", responseEntity.getStatusCode());
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String largestPicUrl = service.findLargestPic(responseEntity.getBody());
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create(largestPicUrl)).build();
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Try again later");
    }

    private URI createUri(String baseUrl, Map<String,?> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        params.forEach(uriBuilder::queryParam);
        return uriBuilder.build().toUri();
    }

    private Map<String, ?> params(Integer sol) {
        Map<String, Object> map = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("sol", sol);
        return map;
    }
}
