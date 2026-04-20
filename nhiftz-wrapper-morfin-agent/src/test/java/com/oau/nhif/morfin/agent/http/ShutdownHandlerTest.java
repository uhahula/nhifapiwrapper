package com.oau.nhif.morfin.agent.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ShutdownHandlerTest {

    private HttpServer server;

    @AfterEach
    void tearDown() { if (server != null) server.stop(0); }

    @Test
    void invokesShutdownCallbackOnPost() throws Exception {
        AtomicBoolean called = new AtomicBoolean(false);
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/shutdown", new ShutdownHandler(() -> called.set(true)));
        server.start();
        int port = server.getAddress().getPort();

        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/shutdown"))
                .POST(HttpRequest.BodyPublishers.noBody()).build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        Thread.sleep(200);
        assertTrue(called.get());
    }
}
