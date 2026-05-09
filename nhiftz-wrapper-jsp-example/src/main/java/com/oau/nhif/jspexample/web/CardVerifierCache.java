package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.CardVerifier;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardVerifierCache {
    private static final Logger LOG = Logger.getLogger(CardVerifierCache.class.getName());

    private static final List<CardVerifier> FALLBACK = List.of();

    private final Supplier<CompletableFuture<List<CardVerifier>>> source;
    private final Clock clock;
    private final Duration ttl;

    private volatile List<CardVerifier> cached;
    private volatile Instant expiresAt = Instant.MIN;

    public CardVerifierCache(Supplier<CompletableFuture<List<CardVerifier>>> source,
                             Clock clock, Duration ttl) {
        this.source = source;
        this.clock = clock;
        this.ttl = ttl;
    }

    public synchronized List<CardVerifier> get() {
        if (cached != null && clock.instant().isBefore(expiresAt)) return cached;
        try {
            cached = source.get().get();
            expiresAt = clock.instant().plus(ttl);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "getCardVerifiers failed, using empty fallback", e);
            cached = FALLBACK;
            expiresAt = clock.instant().plus(Duration.ofMinutes(5));
        }
        return cached;
    }
}
