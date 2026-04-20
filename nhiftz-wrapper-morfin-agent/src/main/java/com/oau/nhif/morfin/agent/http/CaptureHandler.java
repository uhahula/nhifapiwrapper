package com.oau.nhif.morfin.agent.http;

import com.mantra.morfinauth.enums.TemplateFormat;
import com.oau.nhif.morfin.agent.device.CaptureException;
import com.oau.nhif.morfin.agent.device.CaptureResult;
import com.oau.nhif.morfin.agent.device.MorfinDevice;
import com.oau.nhif.morfin.agent.util.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class CaptureHandler implements HttpHandler {
    private final MorfinDevice device;

    public CaptureHandler(MorfinDevice device) { this.device = device; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        String body = readBody(ex.getRequestBody());
        Map<String, String> req = body.isEmpty()
            ? Map.of()
            : Json.readFlatObject(body);

        int timeoutMs = parseInt(req.get("timeoutMs"), 10000);
        int minQuality = parseInt(req.get("minQuality"), 60);
        String formatStr = req.getOrDefault("templateFormat", "ANSI_V378");

        TemplateFormat format;
        try { format = TemplateFormat.valueOf(formatStr); }
        catch (IllegalArgumentException iae) {
            reply(ex, 400, Map.of("error", "INVALID_TEMPLATE_FORMAT"));
            return;
        }

        Map<String, Object> out = new LinkedHashMap<>();
        try {
            CaptureResult r = device.capture(timeoutMs, minQuality, format);
            out.put("template", r.templateBase64());
            out.put("quality", r.quality());
            out.put("nfiq", r.nfiq());
            reply(ex, 200, out);
        } catch (CaptureException ce) {
            out.put("error", ce.kind().name());
            out.put("message", ce.getMessage());
            reply(ex, 200, out);
        }
    }

    private static int parseInt(String s, int fallback) {
        if (s == null || s.isEmpty()) return fallback;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return fallback; }
    }

    private static String readBody(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
    }

    private static void reply(HttpExchange ex, int status, Map<String, ?> body) throws IOException {
        byte[] out = Json.writeObject(body).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(status, out.length);
        ex.getResponseBody().write(out);
        ex.close();
    }
}
