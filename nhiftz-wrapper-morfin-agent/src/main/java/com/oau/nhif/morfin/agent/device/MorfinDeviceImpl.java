package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.MorfinAuthNative;
import com.mantra.morfinauth.enums.DeviceModel;
import com.mantra.morfinauth.enums.TemplateFormat;

import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class MorfinDeviceImpl implements MorfinDevice {
    private static final Logger LOG = Logger.getLogger(MorfinDeviceImpl.class.getName());
    private static final DeviceModel MODEL = DeviceModel.MFS500;

    private final MorfinAuth sdk;
    private final ReentrantLock captureLock = new ReentrantLock();

    private volatile DeviceInfo deviceInfo;
    private volatile boolean initialized;

    public MorfinDeviceImpl() {
        this.sdk = new MorfinAuth(new NoOpCallback());
        startBackgroundInit();
    }

    private void startBackgroundInit() {
        Thread t = new Thread(this::initLoop, "morfin-init");
        t.setDaemon(true);
        t.start();
    }

    private void initLoop() {
        while (!initialized) {
            try {
                if (sdk.IsDeviceConnected(MODEL)) {
                    DeviceInfo info = new DeviceInfo();
                    int rc = sdk.Init(MODEL, null, info);
                    if (rc == 0) {
                        this.deviceInfo = info;
                        this.initialized = true;
                        LOG.info("MFS500 initialized: serial=" + info.SerialNo);
                        return;
                    }
                    LOG.warning("Init returned " + rc + ": " + sdk.GetErrorMessage(rc));
                }
            } catch (Throwable t) {
                LOG.warning("init attempt failed: " + t.getMessage());
            }
            try { Thread.sleep(2000); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Override
    public DeviceStatus status() {
        if (!initialized || deviceInfo == null)
            return DeviceStatus.notReady("DEVICE_NOT_CONNECTED");
        return DeviceStatus.ready(MODEL.name(), deviceInfo.SerialNo, sdk.GetSDKVersion());
    }

    @Override
    public CaptureResult capture(int timeoutMs, int minQuality, TemplateFormat format) throws CaptureException {
        if (!initialized)
            throw new CaptureException(CaptureException.Kind.DEVICE_NOT_CONNECTED, "device not initialized");
        if (!captureLock.tryLock())
            throw new CaptureException(CaptureException.Kind.BUSY, "another capture in progress");
        try {
            int[] quality = new int[1];
            int[] nfiq = new int[1];
            int rc = sdk.AutoCapture(minQuality, timeoutMs, quality, nfiq);
            if (rc != 0) {
                String msg = sdk.GetErrorMessage(rc);
                CaptureException.Kind kind = classify(rc, msg);
                throw new CaptureException(kind, "AutoCapture rc=" + rc + ": " + msg);
            }
            int bufSize = deviceInfo.Width * deviceInfo.Height;
            byte[] buf = new byte[bufSize];
            int[] len = new int[]{bufSize};
            int tr = sdk.GetTemplate(buf, len, format);
            if (tr != 0)
                throw new CaptureException(CaptureException.Kind.SDK_ERROR,
                    "GetTemplate rc=" + tr + ": " + sdk.GetErrorMessage(tr));
            byte[] trimmed = new byte[len[0]];
            System.arraycopy(buf, 0, trimmed, 0, len[0]);
            return new CaptureResult(
                Base64.getEncoder().encodeToString(trimmed),
                quality[0], nfiq[0]);
        } finally {
            captureLock.unlock();
        }
    }

    private CaptureException.Kind classify(int rc, String msg) {
        String m = (msg == null ? "" : msg.toLowerCase());
        if (m.contains("timeout")) return CaptureException.Kind.TIMEOUT;
        if (m.contains("quality")) return CaptureException.Kind.LOW_QUALITY;
        if (rc == MorfinAuthNative.DEVICE_NOT_INITIALIZED)
            return CaptureException.Kind.DEVICE_NOT_CONNECTED;
        return CaptureException.Kind.SDK_ERROR;
    }

    @Override
    public void close() {
        try { sdk.StopCapture(); } catch (Throwable ignored) {}
        try { sdk.Uninit(); } catch (Throwable ignored) {}
    }
}
