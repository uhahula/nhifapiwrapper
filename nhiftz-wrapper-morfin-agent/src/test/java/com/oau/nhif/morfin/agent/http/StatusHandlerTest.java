package com.oau.nhif.morfin.agent.http;

import com.oau.nhif.morfin.agent.device.FakeMorfinDevice;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

class StatusHandlerTest {

    private HttpServer server;

    @AfterEach
    void tearDown() { if (server != null) server.stop(0); }

    private int startWith(StatusHandler h) throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/status", h);
        server.start();
        return server.getAddress().getPort();
    }

    @Test
    void reportsReadyWhenDeviceReady() throws Exception {
        int port = startWith(new StatusHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/status")).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"ready\":true"));
        assertTrue(r.body().contains("\"deviceModel\":\"MFS500\""));
    }

    @Test
    void reportsNotReadyWhenDeviceUnavailable() throws Exception {
        FakeMorfinDevice fake = new FakeMorfinDevice().withReady(false, "DEVICE_NOT_CONNECTED");
        int port = startWith(new StatusHandler(fake));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/status")).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"ready\":false"));
        assertTrue(r.body().contains("\"reason\":\"DEVICE_NOT_CONNECTED\""));
    }

    @Test
    void rejectsNonGet() throws Exception {
        int port = startWith(new StatusHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/status"))
                .POST(HttpRequest.BodyPublishers.noBody()).build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(405, r.statusCode());
    }
}
