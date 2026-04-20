package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.enums.TemplateFormat;

public interface MorfinDevice extends AutoCloseable {
    DeviceStatus status();
    CaptureResult capture(int timeoutMs, int minQuality, TemplateFormat format) throws CaptureException;
    @Override void close();
}
