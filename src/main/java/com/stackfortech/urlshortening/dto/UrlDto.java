package com.stackfortech.urlshortening.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UrlDto {

    @NotBlank(message = "url is required")
    @Pattern(regexp = "https?://.+", message = "url must start with http:// or https://")
    private String url;

    /** Optional ISO-8601 date-time, e.g. 2026-12-31T23:59:59. */
    private String expirationDate;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
}
