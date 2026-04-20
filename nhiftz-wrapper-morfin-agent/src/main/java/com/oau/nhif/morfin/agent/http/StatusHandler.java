package com.oau.nhif.morfin.agent.http;

import com.oau.nhif.morfin.agent.device.DeviceStatus;
import com.oau.nhif.morfin.agent.device.MorfinDevice;
import com.oau.nhif.morfin.agent.util.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatusHandler implements HttpHandler {
    private final MorfinDevice device;

    public StatusHandler(MorfinDevice device) { this.device = device; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        DeviceStatus s = device.status();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ready", s.ready());
        body.put("deviceModel", s.deviceModel());
        body.put("serial", s.serial());
        body.put("sdkVersion", s.sdkVersion());
        body.put("reason", s.reason());
        byte[] out = Json.writeObject(body).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, out.length);
        ex.getResponseBody().write(out);
        ex.close();
    }
}
