package com.example.nasalargestpicfinderservice.service;

import com.example.nasalargestpicfinderservice.service.dto.Photo;
import com.example.nasalargestpicfinderservice.service.dto.Photos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class LargestPicFinderService {

    @Autowired
    private RestTemplate restTemplate;

    public String findLargestPic(Photos body) {
        long maxSize = 0;
        String url = "";
        for (Photo photo : body.getPhotos()) {
            HttpHeaders headers = restTemplate.headForHeaders(photo.getImg_src());
            log.info("checking photo @ {}", photo.getImg_src());
            while (headers.getLocation() != null) {
                headers = restTemplate.headForHeaders(headers.getLocation());
                log.info("checking photo @ {}", photo.getImg_src());
            }
            long contentLength = headers.getContentLength();
            log.info("size is {}", contentLength);
            if (contentLength > maxSize) {
                maxSize = contentLength;
                url = photo.getImg_src();
            }
        }
        log.info("the largest pic is @ {}", url);
        return url;
    }
}
