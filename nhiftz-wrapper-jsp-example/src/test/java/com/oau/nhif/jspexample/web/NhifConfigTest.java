package com.oau.nhif.jspexample.web;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class NhifConfigTest {

    @Test
    void buildsConfigFromCompleteEnv() {
        Map<String, String> env = Map.of(
            "NHIF_AUTH_URL",      "https://test.nhif.or.tz",
            "NHIF_SERVICE_URL",   "https://test.nhif.or.tz/servicehub",
            "NHIF_CLIENT_ID",     "11014",
            "NHIF_CLIENT_SECRET", "secret==",
            "NHIF_USERNAME",      "Mtundi"
        );
        NhifConfig cfg = NhifConfig.fromEnv(env::get).orElseThrow();
        assertEquals("https://test.nhif.or.tz", cfg.authUrl());
        assertEquals("11014", cfg.clientId());
        assertEquals("Mtundi", cfg.username());
    }

    @Test
    void returnsEmptyWhenAnyRequiredVarMissing() {
        Map<String, String> env = Map.of(
            "NHIF_AUTH_URL", "https://test.nhif.or.tz",
            "NHIF_CLIENT_ID", "11014"
        );
        assertTrue(NhifConfig.fromEnv(env::get).isEmpty());
    }

    @Test
    void emptyStringTreatedAsMissing() {
        Map<String, String> env = Map.of(
            "NHIF_AUTH_URL",      "https://test.nhif.or.tz",
            "NHIF_SERVICE_URL",   "https://test.nhif.or.tz/servicehub",
            "NHIF_CLIENT_ID",     "",
            "NHIF_CLIENT_SECRET", "secret==",
            "NHIF_USERNAME",      "Mtundi"
        );
        assertTrue(NhifConfig.fromEnv(env::get).isEmpty());
    }
}
