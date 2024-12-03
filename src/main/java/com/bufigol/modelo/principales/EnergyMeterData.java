package com.bufigol.modelo.principales;

public class EnergyMeterData {
    private int id;
    private double aTotalActEnergy;
    private double aTotalActRetEnergy;
    private double bTotalActEnergy;
    private double bTotalActRetEnergy;
    private double cTotalActEnergy;
    private double cTotalActRetEnergy;
    private double totalAct;
    private double totalActRet;

    public EnergyMeterData(int id, double aTotalActEnergy, double aTotalActRetEnergy, double bTotalActEnergy,
                           double bTotalActRetEnergy, double cTotalActEnergy, double cTotalActRetEnergy,
                           double totalAct, double totalActRet) {
        this.id = id;
        this.aTotalActEnergy = aTotalActEnergy;
        this.aTotalActRetEnergy = aTotalActRetEnergy;
        this.bTotalActEnergy = bTotalActEnergy;
        this.bTotalActRetEnergy = bTotalActRetEnergy;
        this.cTotalActEnergy = cTotalActEnergy;
        this.cTotalActRetEnergy = cTotalActRetEnergy;
        this.totalAct = totalAct;
        this.totalActRet = totalActRet;
    }

    public EnergyMeterData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getaTotalActEnergy() {
        return aTotalActEnergy;
    }

    public void setaTotalActEnergy(double aTotalActEnergy) {
        this.aTotalActEnergy = aTotalActEnergy;
    }

    public double getaTotalActRetEnergy() {
        return aTotalActRetEnergy;
    }

    public void setaTotalActRetEnergy(double aTotalActRetEnergy) {
        this.aTotalActRetEnergy = aTotalActRetEnergy;
    }

    public double getbTotalActEnergy() {
        return bTotalActEnergy;
    }

    public void setbTotalActEnergy(double bTotalActEnergy) {
        this.bTotalActEnergy = bTotalActEnergy;
    }

    public double getbTotalActRetEnergy() {
        return bTotalActRetEnergy;
    }

    public void setbTotalActRetEnergy(double bTotalActRetEnergy) {
        this.bTotalActRetEnergy = bTotalActRetEnergy;
    }

    public double getcTotalActEnergy() {
        return cTotalActEnergy;
    }

    public void setcTotalActEnergy(double cTotalActEnergy) {
        this.cTotalActEnergy = cTotalActEnergy;
    }

    public double getcTotalActRetEnergy() {
        return cTotalActRetEnergy;
    }

    public void setcTotalActRetEnergy(double cTotalActRetEnergy) {
        this.cTotalActRetEnergy = cTotalActRetEnergy;
    }

    public double getTotalAct() {
        return totalAct;
    }

    public void setTotalAct(double totalAct) {
        this.totalAct = totalAct;
    }

    public double getTotalActRet() {
        return totalActRet;
    }

    public void setTotalActRet(double totalActRet) {
        this.totalActRet = totalActRet;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EnergyMeterData that)){
            return false;
        }else{
            return getId() == that.getId() && Double.compare(getaTotalActEnergy(),
                    that.getaTotalActEnergy()) == 0 && Double.compare(getaTotalActRetEnergy(),
                    that.getaTotalActRetEnergy()) == 0 && Double.compare(getbTotalActEnergy(),
                    that.getbTotalActEnergy()) == 0 && Double.compare(getbTotalActRetEnergy(),
                    that.getbTotalActRetEnergy()) == 0 && Double.compare(getcTotalActEnergy(),
                    that.getcTotalActEnergy()) == 0 && Double.compare(getcTotalActRetEnergy(),
                    that.getcTotalActRetEnergy()) == 0 && Double.compare(getTotalAct(),
                    that.getTotalAct()) == 0 && Double.compare(getTotalActRet(),
                    that.getTotalActRet()) == 0;
        }

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + Double.hashCode(getaTotalActEnergy());
        result = 31 * result + Double.hashCode(getaTotalActRetEnergy());
        result = 31 * result + Double.hashCode(getbTotalActEnergy());
        result = 31 * result + Double.hashCode(getbTotalActRetEnergy());
        result = 31 * result + Double.hashCode(getcTotalActEnergy());
        result = 31 * result + Double.hashCode(getcTotalActRetEnergy());
        result = 31 * result + Double.hashCode(getTotalAct());
        result = 31 * result + Double.hashCode(getTotalActRet());
        return result;
    }

    @Override
    public String toString() {
        return "EnergyMeterData{" + "id=" + id +
                ", aTotalActEnergy=" + aTotalActEnergy +
                ", aTotalActRetEnergy=" + aTotalActRetEnergy +
                ", bTotalActEnergy=" + bTotalActEnergy +
                ", bTotalActRetEnergy=" + bTotalActRetEnergy +
                ", cTotalActEnergy=" + cTotalActEnergy +
                ", cTotalActRetEnergy=" + cTotalActRetEnergy +
                ", totalAct=" + totalAct +
                ", totalActRet=" + totalActRet +
                '}';
    }
}
