package com.oau.nhif.morfin.agent.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ShutdownHandler implements HttpHandler {
    private final Runnable callback;

    public ShutdownHandler(Runnable callback) { this.callback = callback; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        byte[] body = "{\"shuttingDown\":true}".getBytes();
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, body.length);
        ex.getResponseBody().write(body);
        ex.close();
        new Thread(callback, "agent-shutdown").start();
    }
}
