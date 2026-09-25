package com.stackfortech.urlshortening.controller;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackfortech.urlshortening.dto.ErrorResponse;
import com.stackfortech.urlshortening.dto.UrlDto;
import com.stackfortech.urlshortening.dto.UrlResponseDto;
import com.stackfortech.urlshortening.model.Url;
import com.stackfortech.urlshortening.service.UrlService;

import jakarta.validation.Valid;

@RestController
public class UrlShorteningController {

    private final UrlService urlService;

    public UrlShorteningController(UrlService urlService) {
        this.urlService = urlService;
    }


    /*
     * Create short URL
     *
     * POST /generate
     */
    @PostMapping("/generate")
    public ResponseEntity<UrlResponseDto> generateShortLink(
            @Valid @RequestBody UrlDto request) {

        Url savedUrl =
                urlService.generateShortLink(request);

        UrlResponseDto response =
                new UrlResponseDto(
                        savedUrl.getOriginalUrl(),
                        savedUrl.getShortLink(),
                        savedUrl.getExpirationDate()
                );

        return ResponseEntity.ok(response);
    }


    /*
     * Redirect short URL
     *
     * GET /r/{shortLink}
     *
     * Example:
     *
     * http://localhost:8080/r/f4a33ee8
     */
    @GetMapping("/r/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(
            @PathVariable String shortLink) {

        Url url =
                urlService.getEncodedUrl(shortLink);


        /*
         * Short URL doesn't exist
         */
        if (url == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            ErrorResponse.of(
                                    HttpStatus.NOT_FOUND.value(),
                                    "Short URL does not exist"
                            )
                    );
        }


        /*
         * Short URL expired
         */
        if (url.getExpirationDate() != null
                && url.getExpirationDate()
                        .isBefore(LocalDateTime.now())) {

            urlService.deleteShortLink(url);

            return ResponseEntity
                    .status(HttpStatus.GONE)
                    .body(
                            ErrorResponse.of(
                                    HttpStatus.GONE.value(),
                                    "Short URL has expired"
                            )
                    );
        }


        /*
         * Redirect to original URL
         */
        HttpHeaders headers =
                new HttpHeaders();

        headers.setLocation(
                URI.create(
                        url.getOriginalUrl()
                )
        );


        return new ResponseEntity<>(
                headers,
                HttpStatus.FOUND
        );
    }
}