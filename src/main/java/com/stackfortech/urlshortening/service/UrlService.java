package com.stackfortech.urlshortening.service;

import com.stackfortech.urlshortening.dto.UrlDto;
import com.stackfortech.urlshortening.model.Url;

public interface UrlService {
    Url generateShortLink(UrlDto request);
    Url getEncodedUrl(String shortLink);
    void deleteShortLink(Url url);
}
