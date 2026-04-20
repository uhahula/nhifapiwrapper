package com.oau.nhif.morfin.agent.device;

public record DeviceStatus(
    boolean ready,
    String deviceModel,
    String serial,
    String sdkVersion,
    String reason
) {
    public static DeviceStatus notReady(String reason) {
        return new DeviceStatus(false, null, null, null, reason);
    }
    public static DeviceStatus ready(String deviceModel, String serial, String sdkVersion) {
        return new DeviceStatus(true, deviceModel, serial, sdkVersion, null);
    }
}
