package com.oau.nhif.morfin.agent.util;

import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @Test
    void writesFlatObjectWithStringsIntsBooleans() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ready", true);
        m.put("quality", 72);
        m.put("deviceModel", "MFS500");
        assertEquals(
            "{\"ready\":true,\"quality\":72,\"deviceModel\":\"MFS500\"}",
            Json.writeObject(m));
    }

    @Test
    void escapesQuotesAndBackslashesInStringValues() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("reason", "device said \"no\" \\ goodbye");
        assertEquals(
            "{\"reason\":\"device said \\\"no\\\" \\\\ goodbye\"}",
            Json.writeObject(m));
    }

    @Test
    void writesNullValuesAsJsonNull() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("serial", null);
        assertEquals("{\"serial\":null}", Json.writeObject(m));
    }

    @Test
    void parsesFlatObjectFromRequestBody() {
        String body = "{\"timeoutMs\":10000,\"minQuality\":60,\"templateFormat\":\"ANSI_V378\"}";
        Map<String, String> out = Json.readFlatObject(body);
        assertEquals("10000", out.get("timeoutMs"));
        assertEquals("60", out.get("minQuality"));
        assertEquals("ANSI_V378", out.get("templateFormat"));
    }

    @Test
    void parsesEmptyObject() {
        assertTrue(Json.readFlatObject("{}").isEmpty());
    }

    @Test
    void parserRejectsNested() {
        assertThrows(IllegalArgumentException.class,
            () -> Json.readFlatObject("{\"a\":{\"b\":1}}"));
    }
}
