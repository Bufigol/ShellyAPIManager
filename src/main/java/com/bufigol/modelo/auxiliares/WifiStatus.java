package com.bufigol.modelo.auxiliares;

import java.util.Objects;

public class WifiStatus {
    private String staIp;
    private String status;
    private String ssid;
    private int rssi;

    public WifiStatus(String staIp, String status, String ssid, int rssi) {
        this.staIp = staIp;
        this.status = status;
        this.ssid = ssid;
        this.rssi = rssi;
    }

    public WifiStatus() {
    }


    /**
     * Calcula la distancia a partir del RSSI utilizando la fórmula estándar.
     *
     * @param rssi  La intensidad de la señal recibida en dBm.
     * @param A     La constante de calibración en dBm.
     * @param N     El exponente de pérdida de trayectoria (entre 2.7 y 4.3).
     * @return La distancia en metros.
     */
    public static double calculateDistance(double rssi, double A, double N) {
        // Evitar división por cero
        if (N <= 0) {
            throw new IllegalArgumentException("El exponente de pérdida de trayectoria N debe ser mayor que 0.");
        }
        return Math.pow(10, (rssi - A) / (10 * N));
    }


    public String getStaIp() {
        return staIp;
    }

    public void setStaIp(String staIp) {
        this.staIp = staIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WifiStatus that)) return false;
        return getRssi() == that.getRssi() && Objects.equals(getStaIp(), that.getStaIp()) && Objects.equals(getStatus(), that.getStatus()) && Objects.equals(getSsid(), that.getSsid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStaIp(), getStatus(), getSsid(), getRssi());
    }

    @Override
    public String toString() {
        return "WifiStatus{" + "staIp='" + staIp + '\'' +
                ", status='" + status + '\'' +
                ", ssid='" + ssid + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}
