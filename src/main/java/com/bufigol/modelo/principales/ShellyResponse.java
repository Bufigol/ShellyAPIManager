package com.bufigol.modelo.principales;

public class ShellyResponse {
    private boolean isok;
    private DeviceData data;

    public ShellyResponse(boolean isok, DeviceData data) {
        this.isok = isok;
        this.data = data;
    }

    public ShellyResponse() {
    }

    public boolean isIsok() {
        return isok;
    }

    public void setIsok(boolean isok) {
        this.isok = isok;
    }

    public DeviceData getData() {
        return data;
    }

    public void setData(DeviceData data) {
        this.data = data;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ShellyResponse that)){
            return false;
        }else{
            return isIsok() == that.isIsok() && getData().equals(that.getData());
        }
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(isIsok());
        result = 31 * result + getData().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ShellyResponse{" + "isok=" + isok +
                ", data=" + data +
                '}';
    }
}
