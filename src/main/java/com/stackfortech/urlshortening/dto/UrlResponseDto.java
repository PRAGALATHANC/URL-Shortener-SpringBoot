package com.stackfortech.urlshortening.dto;

import java.time.LocalDateTime;

public record UrlResponseDto(String originalUrl, String shortLink, LocalDateTime expirationDate) {
}
