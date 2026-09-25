package com.stackfortech.urlshortening;

import com.stackfortech.urlshortening.dto.UrlDto;
import com.stackfortech.urlshortening.model.Url;
import com.stackfortech.urlshortening.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UrlShorteningServiceApplicationTests {

    @Autowired
    private UrlService urlService;

    @Test
    void contextLoads() {
        assertThat(urlService).isNotNull();
    }

    @Test
    void generatesAndFindsShortUrl() {
        UrlDto request = new UrlDto();
        request.setUrl("https://example.com/hello");

        Url saved = urlService.generateShortLink(request);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getShortLink()).hasSize(8);
        assertThat(urlService.getEncodedUrl(saved.getShortLink())).isNotNull();
    }
}
