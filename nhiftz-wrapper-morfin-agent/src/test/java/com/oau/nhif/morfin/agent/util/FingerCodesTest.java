package com.oau.nhif.morfin.agent.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FingerCodesTest {

    @Test
    void exposesAllTenNhifCodes() {
        assertEquals(10, FingerCodes.ALL.size());
        assertTrue(FingerCodes.ALL.contains("R_INDEX"));
        assertTrue(FingerCodes.ALL.contains("L_LITTLE"));
    }

    @Test
    void isValidAcceptsAllKnownCodes() {
        for (String c : FingerCodes.ALL) assertTrue(FingerCodes.isValid(c));
    }

    @Test
    void isValidRejectsUnknownInputs() {
        assertFalse(FingerCodes.isValid("THUMB"));
        assertFalse(FingerCodes.isValid(null));
        assertFalse(FingerCodes.isValid(""));
    }

    @Test
    void humanNameRendersReadable() {
        assertEquals("Right Index", FingerCodes.humanName("R_INDEX"));
        assertEquals("Left Little", FingerCodes.humanName("L_LITTLE"));
    }
}
