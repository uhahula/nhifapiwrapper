package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.MorfinAuth_Callback;
import com.mantra.morfinauth.enums.DeviceDetection;
import com.mantra.morfinauth.enums.FingerPostion;

public class NoOpCallback implements MorfinAuth_Callback {
    @Override public void OnDeviceDetection(String deviceModel, DeviceDetection detection) {}
    @Override public void OnPreview(int width, int height, byte[] image) {}
    @Override public void OnComplete(int quality, int nfiq, int errorCode) {}
    @Override public void OnFingerPostionDetection(int retry, FingerPostion position) {}
}
