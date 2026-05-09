package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;

import javax.servlet.ServletContext;
import java.time.Clock;
import java.time.Duration;

public final class CardVerifierCacheProvider {
    private CardVerifierCacheProvider() {}

    public static final String ATTR = "cardVerifierCache";

    public static CardVerifierCache get(ServletContext ctx) {
        CardVerifierCache existing = (CardVerifierCache) ctx.getAttribute(ATTR);
        if (existing != null) return existing;

        NhifApiClient client = (NhifApiClient) ctx.getAttribute(NhifClientContextListener.CLIENT_ATTR);
        if (client == null) return null;

        CardVerifierCache cache = new CardVerifierCache(
            () -> {
                try { return client.getCardVerifiers(); }
                catch (Exception e) {
                    java.util.concurrent.CompletableFuture<java.util.List<com.oau.nhif.client.model.CardVerifier>> f
                        = new java.util.concurrent.CompletableFuture<>();
                    f.completeExceptionally(e);
                    return f;
                }
            }, Clock.systemUTC(), Duration.ofHours(1));
        ctx.setAttribute(ATTR, cache);
        return cache;
    }
}
