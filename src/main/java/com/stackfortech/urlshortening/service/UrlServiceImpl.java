package com.stackfortech.urlshortening.service;

import com.stackfortech.urlshortening.dto.UrlDto;
import com.stackfortech.urlshortening.model.Url;
import com.stackfortech.urlshortening.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HexFormat;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url generateShortLink(UrlDto request) {
        validateUrl(request.getUrl());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = parseExpiration(request.getExpirationDate(), now);
        String shortCode = createUniqueShortCode(request.getUrl(), now);

        return urlRepository.save(new Url(
                request.getUrl(),
                shortCode,
                now,
                expiration
        ));
    }

    private void validateUrl(String value) {
        try {
            URI uri = URI.create(value);
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))
                    || uri.getHost() == null) {
                throw new IllegalArgumentException("url must be a valid http:// or https:// URL");
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("url must be a valid http:// or https:// URL");
        }
    }

    private LocalDateTime parseExpiration(String value, LocalDateTime now) {
        if (value == null || value.isBlank()) {
            return now.plusMinutes(10);
        }
        try {
            LocalDateTime expiration = LocalDateTime.parse(value);
            if (!expiration.isAfter(now)) {
                throw new IllegalArgumentException("expirationDate must be in the future");
            }
            return expiration;
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "expirationDate must use ISO-8601 format, for example 2026-12-31T23:59:59"
            );
        }
    }

    private String createUniqueShortCode(String originalUrl, LocalDateTime now) {
        String input = originalUrl + ":" + now + ":" + System.nanoTime();
        String hash = sha256(input);
        String code = hash.substring(0, 8);

        int suffix = 0;
        while (urlRepository.findByShortLink(code).isPresent()) {
            suffix++;
            code = hash.substring(0, 7) + Integer.toString(suffix, 36);
        }
        return code;
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable", ex);
        }
    }

    @Override
    public Url getEncodedUrl(String shortLink) {
        return urlRepository.findByShortLink(shortLink).orElse(null);
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}
