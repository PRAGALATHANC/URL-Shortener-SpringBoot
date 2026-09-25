#!/bin/sh
pkill -f 'url-shortener.*jar' 2>/dev/null || true
pkill -f 'UrlShorteningServiceApplication' 2>/dev/null || true
echo "Application stop requested."
