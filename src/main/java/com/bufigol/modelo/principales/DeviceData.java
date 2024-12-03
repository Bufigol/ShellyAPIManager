package com.bufigol.modelo.principales;

public class DeviceData {
    private boolean online;
    private DeviceStatus deviceStatus;

    public DeviceData(boolean online, DeviceStatus deviceStatus) {
        this.online = online;
        this.deviceStatus = deviceStatus;
    }

    public DeviceData() {
        this.online = false;
        this.deviceStatus = new DeviceStatus();
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof DeviceData that)){
            return false;
        }else{
            return isOnline() == that.isOnline() && getDeviceStatus().equals(that.getDeviceStatus());
        }
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(isOnline());
        result = 31 * result + getDeviceStatus().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DeviceData{" + "online=" + online +
                ", deviceStatus=" + deviceStatus +
                '}';
    }
}
