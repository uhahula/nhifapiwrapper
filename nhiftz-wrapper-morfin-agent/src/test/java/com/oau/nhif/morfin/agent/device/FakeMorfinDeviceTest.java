package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.enums.TemplateFormat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FakeMorfinDeviceTest {

    @Test
    void defaultFakeIsReadyAndReturnsCannedTemplate() throws Exception {
        FakeMorfinDevice fake = new FakeMorfinDevice();
        DeviceStatus s = fake.status();
        assertTrue(s.ready());
        assertEquals("MFS500", s.deviceModel());

        CaptureResult r = fake.capture(5000, 60, TemplateFormat.ANSI_V378);
        assertNotNull(r.templateBase64());
        assertEquals(72, r.quality());
        assertEquals(2, r.nfiq());
    }

    @Test
    void fakeCanBeConfiguredToReportNotReady() {
        FakeMorfinDevice fake = new FakeMorfinDevice().withReady(false, "DEVICE_NOT_CONNECTED");
        DeviceStatus s = fake.status();
        assertFalse(s.ready());
        assertEquals("DEVICE_NOT_CONNECTED", s.reason());
    }

    @Test
    void fakeCanBeConfiguredToThrowOnCapture() {
        FakeMorfinDevice fake = new FakeMorfinDevice().withCaptureFailure(CaptureException.Kind.TIMEOUT);
        CaptureException ex = assertThrows(CaptureException.class,
            () -> fake.capture(1000, 60, TemplateFormat.ANSI_V378));
        assertEquals(CaptureException.Kind.TIMEOUT, ex.kind());
    }
}
