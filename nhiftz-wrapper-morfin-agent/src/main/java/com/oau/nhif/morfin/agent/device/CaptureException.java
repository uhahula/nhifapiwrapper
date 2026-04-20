package com.oau.nhif.morfin.agent.device;

public class CaptureException extends Exception {
    public enum Kind { DEVICE_NOT_CONNECTED, TIMEOUT, LOW_QUALITY, BUSY, SDK_ERROR }

    private final Kind kind;

    public CaptureException(Kind kind, String message) {
        super(message);
        this.kind = kind;
    }
    public Kind kind() { return kind; }
}
