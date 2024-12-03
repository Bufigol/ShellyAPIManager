package com.bufigol.modelo.auxiliares;

import java.util.Map;

public class JSONResponse {
    private boolean isok;
    private Map<String, Object> data;

    public JSONResponse() {
    }

    public JSONResponse(boolean isok, Map<String, Object> data) {
        this.isok = isok;
        this.data = data;
    }

    public boolean isIsok() {
        return isok;
    }

    public void setIsok(boolean isok) {
        this.isok = isok;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JSONResponse that)) {
            return false;
        }
        return isIsok() == that.isIsok() && getData().equals(that.getData());
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(isIsok());
        result = 31 * result + getData().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "JSONResponse{" +
                "isok=" + isok +
                ", data=" + data +
                '}';
    }
}