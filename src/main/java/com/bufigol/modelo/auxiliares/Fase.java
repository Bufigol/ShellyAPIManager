package com.bufigol.modelo.auxiliares;

public class Fase {
    private double actPower;    // Potencia activa
    private double aprtPower;   // Potencia aparente
    private double current;     // Corriente
    private double freq;        // Frecuencia
    private double pf;          // Factor de potencia
    private double voltage;     // Voltaje

    public Fase(double actPower, double aprtPower, double current, double freq, double pf, double voltage) {
        this.actPower = actPower;
        this.aprtPower = aprtPower;
        this.current = current;
        this.freq = freq;
        this.pf = pf;
        this.voltage = voltage;
    }

    public Fase() {
    }

    public double getActPower() {
        return actPower;
    }

    public void setActPower(double actPower) {
        this.actPower = actPower;
    }

    public double getAprtPower() {
        return aprtPower;
    }

    public void setAprtPower(double aprtPower) {
        this.aprtPower = aprtPower;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public double getPf() {
        return pf;
    }

    public void setPf(double pf) {
        this.pf = pf;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Fase fase)){
            return false;
        }else{
            return Double.compare(getActPower(), fase.getActPower()) == 0 && Double.compare(getAprtPower(),
                    fase.getAprtPower()) == 0 && Double.compare(getCurrent(),
                    fase.getCurrent()) == 0 && Double.compare(getFreq(),
                    fase.getFreq()) == 0 && Double.compare(getPf(),
                    fase.getPf()) == 0 && Double.compare(getVoltage(),
                    fase.getVoltage()) == 0;
        }
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(getActPower());
        result = 31 * result + Double.hashCode(getAprtPower());
        result = 31 * result + Double.hashCode(getCurrent());
        result = 31 * result + Double.hashCode(getFreq());
        result = 31 * result + Double.hashCode(getPf());
        result = 31 * result + Double.hashCode(getVoltage());
        return result;
    }

    @Override
    public String toString() {
        return "Fase{" + "actPower=" + actPower +
                ", aprtPower=" + aprtPower +
                ", current=" + current +
                ", freq=" + freq +
                ", pf=" + pf +
                ", voltage=" + voltage +
                '}';
    }
}
