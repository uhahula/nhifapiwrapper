package com.oau.nhif.morfin.agent.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Json {
    private Json() {}

    public static String writeObject(Map<String, ?> map) {
        StringBuilder sb = new StringBuilder(64);
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, ?> e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(escape(e.getKey())).append("\":");
            Object v = e.getValue();
            if (v == null) sb.append("null");
            else if (v instanceof Boolean) sb.append(v);
            else if (v instanceof Number) sb.append(v);
            else sb.append('"').append(escape(v.toString())).append('"');
        }
        sb.append('}');
        return sb.toString();
    }

    public static Map<String, String> readFlatObject(String body) {
        String s = body.trim();
        if (s.isEmpty() || s.charAt(0) != '{' || s.charAt(s.length() - 1) != '}')
            throw new IllegalArgumentException("not a JSON object: " + body);
        Map<String, String> out = new LinkedHashMap<>();
        int i = 1;
        int end = s.length() - 1;
        while (i < end) {
            while (i < end && Character.isWhitespace(s.charAt(i))) i++;
            if (i >= end) break;
            if (s.charAt(i) != '"')
                throw new IllegalArgumentException("expected key quote at " + i);
            int keyStart = ++i;
            while (i < end && s.charAt(i) != '"') i++;
            String key = s.substring(keyStart, i);
            i++;
            while (i < end && s.charAt(i) != ':') i++;
            i++;
            while (i < end && Character.isWhitespace(s.charAt(i))) i++;
            String value;
            if (s.charAt(i) == '"') {
                i++;
                StringBuilder v = new StringBuilder();
                while (i < end && s.charAt(i) != '"') {
                    if (s.charAt(i) == '\\' && i + 1 < end) {
                        char n = s.charAt(i + 1);
                        if (n == '"') v.append('"');
                        else if (n == '\\') v.append('\\');
                        else v.append(s.charAt(i)).append(n);
                        i += 2;
                    } else {
                        v.append(s.charAt(i));
                        i++;
                    }
                }
                value = v.toString();
                i++;
            } else if (s.charAt(i) == '{' || s.charAt(i) == '[') {
                throw new IllegalArgumentException("nested values not supported");
            } else {
                int vStart = i;
                while (i < end && s.charAt(i) != ',' && !Character.isWhitespace(s.charAt(i))) i++;
                value = s.substring(vStart, i);
            }
            out.put(key, value);
            while (i < end && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ',')) i++;
        }
        return out;
    }

    private static String escape(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') sb.append("\\\"");
            else if (c == '\\') sb.append("\\\\");
            else if (c == '\n') sb.append("\\n");
            else if (c == '\r') sb.append("\\r");
            else if (c == '\t') sb.append("\\t");
            else sb.append(c);
        }
        return sb.toString();
    }
}
