package com.stackfortech.urlshortening.repository;

import com.stackfortech.urlshortening.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortLink(String shortLink);
}
