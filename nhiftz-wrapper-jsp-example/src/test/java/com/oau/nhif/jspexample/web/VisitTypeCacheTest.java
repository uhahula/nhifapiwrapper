package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.VisitType;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class VisitTypeCacheTest {

    private static VisitType vt(int id, String name) {
        VisitType v = new VisitType();
        v.setVisitTypeID(id);
        v.setVisitTypeName(name);
        return v;
    }

    @Test
    void fetchesOnceWithinTtl() {
        AtomicInteger calls = new AtomicInteger();
        VisitTypeCache cache = new VisitTypeCache(() -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(List.of(vt(1, "Normal")));
        }, Clock.systemUTC(), Duration.ofHours(1));

        assertEquals(1, cache.get().size());
        assertEquals(1, cache.get().size());
        assertEquals(1, calls.get());
    }

    @Test
    void refreshesAfterTtl() {
        AtomicInteger calls = new AtomicInteger();
        Instant start = Instant.parse("2026-04-20T00:00:00Z");
        Clock[] nowBox = { Clock.fixed(start, ZoneOffset.UTC) };
        Clock later = Clock.fixed(start.plusSeconds(3601), ZoneOffset.UTC);
        Clock clock = new Clock() {
            @Override public Instant instant() { return nowBox[0].instant(); }
            @Override public java.time.ZoneId getZone() { return ZoneOffset.UTC; }
            @Override public Clock withZone(java.time.ZoneId z) { return this; }
        };
        VisitTypeCache cache = new VisitTypeCache(() -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(List.of(vt(1, "Normal")));
        }, clock, Duration.ofHours(1));

        cache.get();
        nowBox[0] = later;
        cache.get();
        assertEquals(2, calls.get());
    }

    @Test
    void fallsBackToHardcodedOnFetchFailure() {
        VisitTypeCache cache = new VisitTypeCache(() -> {
            CompletableFuture<List<VisitType>> f = new CompletableFuture<>();
            f.completeExceptionally(new RuntimeException("boom"));
            return f;
        }, Clock.systemUTC(), Duration.ofHours(1));

        List<VisitType> result = cache.get();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getVisitTypeID().intValue());
        assertEquals("Normal", result.get(0).getVisitTypeName());
    }
}
