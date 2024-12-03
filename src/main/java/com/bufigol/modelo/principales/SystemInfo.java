package com.bufigol.modelo.principales;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import com.bufigol.modelo.auxiliares.AvailableUpdates;

public class SystemInfo {
    private AvailableUpdates availableUpdates;
    private String mac;
    private boolean restartRequired;
    private LocalTime time;
    private LocalDateTime timestamp;
    private Duration uptime;
    private int ramSize;
    private int ramFree;
    private int fsSize;
    private int fsFree;
    private int cfgRev;
    private int kvsRev;
    private int scheduleRev;
    private int webhookRev;
    private int resetReason;

    public SystemInfo(AvailableUpdates updates, String mac, boolean restart, String timeStr,
                      long unixtime, int uptime, int ramSize, int ramFree, int fsSize,
                      int fsFree, int cfgRev, int kvsRev, int scheduleRev, int webhookRev,
                      int resetReason) {
        this.availableUpdates = updates;
        this.mac = mac;
        this.restartRequired = restart;
        this.time = LocalTime.parse(timeStr);
        this.timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(unixtime),
                ZoneId.systemDefault()
        );
        this.uptime = Duration.ofSeconds(uptime);
        this.ramSize = ramSize;
        this.ramFree = ramFree;
        this.fsSize = fsSize;
        this.fsFree = fsFree;
        this.cfgRev = cfgRev;
        this.kvsRev = kvsRev;
        this.scheduleRev = scheduleRev;
        this.webhookRev = webhookRev;
        this.resetReason = resetReason;
    }

    public SystemInfo() {
    }

    public AvailableUpdates getAvailableUpdates() {
        return availableUpdates;
    }

    public void setAvailableUpdates(AvailableUpdates availableUpdates) {
        this.availableUpdates = availableUpdates;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isRestartRequired() {
        return restartRequired;
    }

    public void setRestartRequired(boolean restartRequired) {
        this.restartRequired = restartRequired;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Duration getUptime() {
        return uptime;
    }

    public void setUptime(Duration uptime) {
        this.uptime = uptime;
    }

    public int getRamSize() {
        return ramSize;
    }

    public void setRamSize(int ramSize) {
        this.ramSize = ramSize;
    }

    public int getRamFree() {
        return ramFree;
    }

    public void setRamFree(int ramFree) {
        this.ramFree = ramFree;
    }

    public int getFsSize() {
        return fsSize;
    }

    public void setFsSize(int fsSize) {
        this.fsSize = fsSize;
    }

    public int getFsFree() {
        return fsFree;
    }

    public void setFsFree(int fsFree) {
        this.fsFree = fsFree;
    }

    public int getCfgRev() {
        return cfgRev;
    }

    public void setCfgRev(int cfgRev) {
        this.cfgRev = cfgRev;
    }

    public int getKvsRev() {
        return kvsRev;
    }

    public void setKvsRev(int kvsRev) {
        this.kvsRev = kvsRev;
    }

    public int getScheduleRev() {
        return scheduleRev;
    }

    public void setScheduleRev(int scheduleRev) {
        this.scheduleRev = scheduleRev;
    }

    public int getWebhookRev() {
        return webhookRev;
    }

    public void setWebhookRev(int webhookRev) {
        this.webhookRev = webhookRev;
    }

    public int getResetReason() {
        return resetReason;
    }

    public void setResetReason(int resetReason) {
        this.resetReason = resetReason;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof SystemInfo that)){
            return false;
        }else {
            return isRestartRequired() == that.isRestartRequired() &&
                    getRamSize() == that.getRamSize() &&
                    getRamFree() == that.getRamFree() &&
                    getFsSize() == that.getFsSize() &&
                    getFsFree() == that.getFsFree() &&
                    getCfgRev() == that.getCfgRev() &&
                    getKvsRev() == that.getKvsRev() &&
                    getScheduleRev() == that.getScheduleRev() &&
                    getWebhookRev() == that.getWebhookRev() &&
                    getResetReason() == that.getResetReason() &&
                    getAvailableUpdates().equals(
                            that.getAvailableUpdates()) && getMac().equals(that.getMac()) &&
                            getTime().equals(that.getTime()) &&
                    getTimestamp().equals(that.getTimestamp()) &&
                    getUptime().equals(that.getUptime());
        }
    }

    @Override
    public int hashCode() {
        int result = getAvailableUpdates().hashCode();
        result = 31 * result + getMac().hashCode();
        result = 31 * result + Boolean.hashCode(isRestartRequired());
        result = 31 * result + getTime().hashCode();
        result = 31 * result + getTimestamp().hashCode();
        result = 31 * result + getUptime().hashCode();
        result = 31 * result + getRamSize();
        result = 31 * result + getRamFree();
        result = 31 * result + getFsSize();
        result = 31 * result + getFsFree();
        result = 31 * result + getCfgRev();
        result = 31 * result + getKvsRev();
        result = 31 * result + getScheduleRev();
        result = 31 * result + getWebhookRev();
        result = 31 * result + getResetReason();
        return result;
    }

    @Override
    public String toString() {
        return "SystemInfo{" + "availableUpdates=" + availableUpdates +
                ", mac='" + mac + '\'' +
                ", restartRequired=" + restartRequired +
                ", time=" + time +
                ", timestamp=" + timestamp +
                ", uptime=" + uptime +
                ", ramSize=" + ramSize +
                ", ramFree=" + ramFree +
                ", fsSize=" + fsSize +
                ", fsFree=" + fsFree +
                ", cfgRev=" + cfgRev +
                ", kvsRev=" + kvsRev +
                ", scheduleRev=" + scheduleRev +
                ", webhookRev=" + webhookRev +
                ", resetReason=" + resetReason +
                '}';
    }
}