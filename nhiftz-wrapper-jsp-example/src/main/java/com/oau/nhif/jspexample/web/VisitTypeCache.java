package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.VisitType;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisitTypeCache {
    private static final Logger LOG = Logger.getLogger(VisitTypeCache.class.getName());

    private static List<VisitType> fallback() {
        VisitType v = new VisitType();
        v.setVisitTypeID(1);
        v.setVisitTypeName("Normal");
        return List.of(v);
    }
    private static final List<VisitType> FALLBACK = fallback();

    private final Supplier<CompletableFuture<List<VisitType>>> source;
    private final Clock clock;
    private final Duration ttl;

    private volatile List<VisitType> cached;
    private volatile Instant expiresAt = Instant.MIN;

    public VisitTypeCache(Supplier<CompletableFuture<List<VisitType>>> source,
                          Clock clock, Duration ttl) {
        this.source = source;
        this.clock = clock;
        this.ttl = ttl;
    }

    public synchronized List<VisitType> get() {
        if (cached != null && clock.instant().isBefore(expiresAt)) return cached;
        try {
            cached = source.get().get();
            expiresAt = clock.instant().plus(ttl);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "getVisitTypes failed, using fallback", e);
            cached = FALLBACK;
            expiresAt = clock.instant().plus(Duration.ofMinutes(5));
        }
        return cached;
    }
}
