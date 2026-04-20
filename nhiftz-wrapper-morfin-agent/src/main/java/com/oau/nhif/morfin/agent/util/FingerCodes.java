package com.oau.nhif.morfin.agent.util;

import java.util.List;

public final class FingerCodes {
    private FingerCodes() {}

    public static final List<String> ALL = List.of(
        "R_THUMB", "R_INDEX", "R_MIDDLE", "R_RING", "R_LITTLE",
        "L_THUMB", "L_INDEX", "L_MIDDLE", "L_RING", "L_LITTLE"
    );

    public static boolean isValid(String code) {
        return code != null && ALL.contains(code);
    }

    public static String humanName(String code) {
        if (!isValid(code)) return code;
        String side = code.startsWith("R_") ? "Right " : "Left ";
        String finger = code.substring(2);
        return side + finger.charAt(0) + finger.substring(1).toLowerCase();
    }
}
