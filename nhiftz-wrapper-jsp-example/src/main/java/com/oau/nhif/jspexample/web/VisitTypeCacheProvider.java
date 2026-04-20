package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;

import javax.servlet.ServletContext;
import java.time.Clock;
import java.time.Duration;

public final class VisitTypeCacheProvider {
    private VisitTypeCacheProvider() {}

    public static final String ATTR = "visitTypeCache";

    public static VisitTypeCache get(ServletContext ctx) {
        VisitTypeCache existing = (VisitTypeCache) ctx.getAttribute(ATTR);
        if (existing != null) return existing;

        NhifApiClient client = (NhifApiClient) ctx.getAttribute(NhifClientContextListener.CLIENT_ATTR);
        if (client == null) return null;

        VisitTypeCache cache = new VisitTypeCache(
            () -> {
                try { return client.getVisitTypes(); }
                catch (Exception e) {
                    java.util.concurrent.CompletableFuture<java.util.List<com.oau.nhif.client.model.VisitType>> f
                        = new java.util.concurrent.CompletableFuture<>();
                    f.completeExceptionally(e);
                    return f;
                }
            }, Clock.systemUTC(), Duration.ofHours(1));
        ctx.setAttribute(ATTR, cache);
        return cache;
    }
}
