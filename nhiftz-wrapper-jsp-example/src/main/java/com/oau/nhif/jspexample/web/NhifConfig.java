package com.oau.nhif.jspexample.web;

import java.util.Optional;
import java.util.function.Function;

public record NhifConfig(
    String authUrl,
    String serviceUrl,
    String clientId,
    String clientSecret,
    String username,
    String facilityCode,
    Integer defaultCardTypeID
) {
    public static Optional<NhifConfig> fromEnv(Function<String, String> lookup) {
        String auth = val(lookup, "NHIF_AUTH_URL");
        String svc = val(lookup, "NHIF_SERVICE_URL");
        String cid = val(lookup, "NHIF_CLIENT_ID");
        String sec = val(lookup, "NHIF_CLIENT_SECRET");
        String usr = val(lookup, "NHIF_USERNAME");
        if (auth == null || svc == null || cid == null || sec == null || usr == null)
            return Optional.empty();
        // Optional fields - the wrapper will leave them null in the JSON
        // if unset, which matches NHIF's documented payload.
        String facility = val(lookup, "NHIF_FACILITY_CODE");
        Integer cardType = parseInt(val(lookup, "NHIF_DEFAULT_CARD_TYPE_ID"));
        return Optional.of(new NhifConfig(auth, svc, cid, sec, usr, facility, cardType));
    }

    private static String val(Function<String, String> lookup, String key) {
        String v = lookup.apply(key);
        return (v == null || v.isEmpty()) ? null : v;
    }

    private static Integer parseInt(String s) {
        if (s == null) return null;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; }
    }
}
