package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.enums.TemplateFormat;
import java.util.Base64;

public class FakeMorfinDevice implements MorfinDevice {
    private boolean ready = true;
    private String notReadyReason;
    private CaptureException.Kind captureFailure;

    public FakeMorfinDevice withReady(boolean ready, String reason) {
        this.ready = ready;
        this.notReadyReason = reason;
        return this;
    }

    public FakeMorfinDevice withCaptureFailure(CaptureException.Kind kind) {
        this.captureFailure = kind;
        return this;
    }

    @Override
    public DeviceStatus status() {
        if (!ready) return DeviceStatus.notReady(notReadyReason);
        return DeviceStatus.ready("MFS500", "FAKE-SERIAL-1", "1.0.0.19-fake");
    }

    @Override
    public CaptureResult capture(int timeoutMs, int minQuality, TemplateFormat format) throws CaptureException {
        if (captureFailure != null)
            throw new CaptureException(captureFailure, "fake failure: " + captureFailure);
        String b64 = Base64.getEncoder().encodeToString("FAKE_TEMPLATE_DATA".getBytes());
        return new CaptureResult(b64, 72, 2);
    }

    @Override
    public void close() { }
}
