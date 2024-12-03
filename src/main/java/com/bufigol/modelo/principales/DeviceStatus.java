package com.bufigol.modelo.principales;

import com.bufigol.modelo.auxiliares.CloudStatus;
import com.bufigol.modelo.auxiliares.Temperature;
import com.bufigol.modelo.auxiliares.WifiStatus;

import java.util.Objects;

public class DeviceStatus {
    private String code;
    private EnergyMeter em0;
    private String updated;
    private CloudStatus cloud;
    private WifiStatus wifi;
    private Temperature temperature0;
    private EnergyMeterData emdata0;
    private SystemInfo sys;
    private String id;

    public DeviceStatus(String code, EnergyMeter em0, String updated, CloudStatus cloud, WifiStatus wifi,
                        Temperature temperature0, EnergyMeterData emdata0, SystemInfo sys, String id) {
        this.code = code;
        this.em0 = em0;
        this.updated = updated;
        this.cloud = cloud;
        this.wifi = wifi;
        this.temperature0 = temperature0;
        this.emdata0 = emdata0;
        this.sys = sys;
        this.id = id;
    }

    public DeviceStatus() {
        this.code = "";
        this.em0 = new EnergyMeter();
        this.updated = "";
        this.cloud = new CloudStatus();
        this.wifi = new WifiStatus();
        this.temperature0 = new Temperature();
        this.emdata0 = new EnergyMeterData();
        this.sys = new SystemInfo();
        this.id = "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EnergyMeter getEm0() {
        return em0;
    }

    public void setEm0(EnergyMeter em0) {
        this.em0 = em0;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public CloudStatus getCloud() {
        return cloud;
    }

    public void setCloud(CloudStatus cloud) {
        this.cloud = cloud;
    }

    public WifiStatus getWifi() {
        return wifi;
    }

    public void setWifi(WifiStatus wifi) {
        this.wifi = wifi;
    }

    public Temperature getTemperature0() {
        return temperature0;
    }

    public void setTemperature0(Temperature temperature0) {
        this.temperature0 = temperature0;
    }

    public EnergyMeterData getEmdata0() {
        return emdata0;
    }

    public void setEmdata0(EnergyMeterData emdata0) {
        this.emdata0 = emdata0;
    }

    public SystemInfo getSys() {
        return sys;
    }

    public void setSys(SystemInfo sys) {
        this.sys = sys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof DeviceStatus that)){
            return false;
        }else{
            return Objects.equals(getCode(), that.getCode()) && Objects.equals(getEm0(), that.getEm0()) && Objects.equals(getUpdated(), that.getUpdated()) && Objects.equals(getCloud(), that.getCloud()) && Objects.equals(getWifi(), that.getWifi()) && Objects.equals(getTemperature0(), that.getTemperature0()) && Objects.equals(getEmdata0(), that.getEmdata0()) && Objects.equals(getSys(), that.getSys()) && getId().equals(that.getId());

        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getCode());
        result = 31 * result + Objects.hashCode(getEm0());
        result = 31 * result + Objects.hashCode(getUpdated());
        result = 31 * result + Objects.hashCode(getCloud());
        result = 31 * result + Objects.hashCode(getWifi());
        result = 31 * result + Objects.hashCode(getTemperature0());
        result = 31 * result + Objects.hashCode(getEmdata0());
        result = 31 * result + Objects.hashCode(getSys());
        result = 31 * result + getId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" + "code='" + code + '\'' +
                ", em0=" + em0 +
                ", updated='" + updated + '\'' +
                ", cloud=" + cloud +
                ", wifi=" + wifi +
                ", temperature0=" + temperature0 +
                ", emdata0=" + emdata0 +
                ", sys=" + sys +
                ", id='" + id + '\'' +
                '}';
    }
}
