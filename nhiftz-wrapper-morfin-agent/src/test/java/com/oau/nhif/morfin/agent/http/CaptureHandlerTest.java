package com.oau.nhif.morfin.agent.http;

import com.oau.nhif.morfin.agent.device.CaptureException;
import com.oau.nhif.morfin.agent.device.FakeMorfinDevice;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class CaptureHandlerTest {

    private HttpServer server;

    @AfterEach
    void tearDown() { if (server != null) server.stop(0); }

    private int startWith(CaptureHandler h) throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/capture", h);
        server.start();
        return server.getAddress().getPort();
    }

    private HttpResponse<String> postJson(int port, String body) throws Exception {
        return HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/capture"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build(),
            HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void returnsTemplateOnSuccess() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = postJson(port,
            "{\"timeoutMs\":5000,\"minQuality\":60,\"templateFormat\":\"ANSI_V378\"}");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"template\":\""));
        assertTrue(r.body().contains("\"quality\":72"));
        assertTrue(r.body().contains("\"nfiq\":2"));
    }

    @Test
    void usesDefaultsWhenFieldsMissing() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = postJson(port, "{}");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"template\":\""));
    }

    @Test
    void returnsErrorJsonOnTimeout() throws Exception {
        FakeMorfinDevice fake = new FakeMorfinDevice()
            .withCaptureFailure(CaptureException.Kind.TIMEOUT);
        int port = startWith(new CaptureHandler(fake));
        HttpResponse<String> r = postJson(port, "{}");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"error\":\"TIMEOUT\""));
    }

    @Test
    void rejectsNonPost() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/capture")).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(405, r.statusCode());
    }

    @Test
    void rejectsInvalidTemplateFormat() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = postJson(port, "{\"templateFormat\":\"BOGUS\"}");
        assertEquals(400, r.statusCode());
    }
}
