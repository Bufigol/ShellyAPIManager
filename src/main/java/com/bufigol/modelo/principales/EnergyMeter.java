package com.bufigol.modelo.principales;

import com.bufigol.modelo.auxiliares.Fase;

import java.util.List;

public class EnergyMeter {
    private int id;
    private Fase faseA;
    private Fase faseB;
    private Fase faseC;
    private double totalActPower;
    private double totalAprtPower;
    private double totalCurrent;
    private List<String> userCalibratedPhase;

    public EnergyMeter(int id, Fase faseA, Fase faseB, Fase faseC, double totalActPower,
                       double totalAprtPower, double totalCurrent, List<String> userCalibratedPhase) {
        this.id = id;
        this.faseA = faseA;
        this.faseB = faseB;
        this.faseC = faseC;
        this.totalActPower = totalActPower;
        this.totalAprtPower = totalAprtPower;
        this.totalCurrent = totalCurrent;
        this.userCalibratedPhase = userCalibratedPhase;
    }

    public EnergyMeter(int id,
                       double aActPower, double aAprtPower, double aCurrent, double aFreq, double aPf, double aVoltage,
                       double bActPower, double bAprtPower, double bCurrent, double bFreq, double bPf, double bVoltage,
                       double cActPower, double cAprtPower, double cCurrent, double cFreq, double cPf, double cVoltage,
                       double totalActPower, double totalAprtPower, double totalCurrent,
                       List<String> userCalibratedPhase) {
        this.id = id;
        this.faseA = new Fase(aActPower, aAprtPower, aCurrent, aFreq, aPf, aVoltage);
        this.faseB = new Fase(bActPower, bAprtPower, bCurrent, bFreq, bPf, bVoltage);
        this.faseC = new Fase(cActPower, cAprtPower, cCurrent, cFreq, cPf, cVoltage);
        this.totalActPower = totalActPower;
        this.totalAprtPower = totalAprtPower;
        this.totalCurrent = totalCurrent;
        this.userCalibratedPhase = userCalibratedPhase;
    }

    public EnergyMeter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fase getFaseA() {
        return faseA;
    }

    public void setFaseA(Fase faseA) {
        this.faseA = faseA;
    }

    public Fase getFaseB() {
        return faseB;
    }

    public void setFaseB(Fase faseB) {
        this.faseB = faseB;
    }

    public Fase getFaseC() {
        return faseC;
    }

    public void setFaseC(Fase faseC) {
        this.faseC = faseC;
    }

    public double getTotalActPower() {
        return totalActPower;
    }

    public void setTotalActPower(double totalActPower) {
        this.totalActPower = totalActPower;
    }

    public double getTotalAprtPower() {
        return totalAprtPower;
    }

    public void setTotalAprtPower(double totalAprtPower) {
        this.totalAprtPower = totalAprtPower;
    }

    public double getTotalCurrent() {
        return totalCurrent;
    }

    public void setTotalCurrent(double totalCurrent) {
        this.totalCurrent = totalCurrent;
    }

    public List<String> getUserCalibratedPhase() {
        return userCalibratedPhase;
    }

    public void setUserCalibratedPhase(List<String> userCalibratedPhase) {
        this.userCalibratedPhase = userCalibratedPhase;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EnergyMeter that)){
            return false;
        }else{
            return getId() == that.getId() &&
                    Double.compare(getTotalActPower(), that.getTotalActPower()) == 0 &&
                    Double.compare(getTotalAprtPower(), that.getTotalAprtPower()) == 0 &&
                    Double.compare(getTotalCurrent(), that.getTotalCurrent()) == 0 &&
                    getFaseA().equals(that.getFaseA()) &&
                    getFaseB().equals(that.getFaseB()) &&
                    getFaseC().equals(that.getFaseC()) &&
                    getUserCalibratedPhase().equals(that.getUserCalibratedPhase());
        }
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getFaseA().hashCode();
        result = 31 * result + getFaseB().hashCode();
        result = 31 * result + getFaseC().hashCode();
        result = 31 * result + Double.hashCode(getTotalActPower());
        result = 31 * result + Double.hashCode(getTotalAprtPower());
        result = 31 * result + Double.hashCode(getTotalCurrent());
        result = 31 * result + getUserCalibratedPhase().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EnergyMeter{" + "id=" + id +
                ", faseA=" + faseA +
                ", faseB=" + faseB +
                ", faseC=" + faseC +
                ", totalActPower=" + totalActPower +
                ", totalAprtPower=" + totalAprtPower +
                ", totalCurrent=" + totalCurrent +
                ", userCalibratedPhase=" + userCalibratedPhase +
                '}';
    }
}
