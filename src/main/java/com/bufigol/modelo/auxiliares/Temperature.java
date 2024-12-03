package com.bufigol.modelo.auxiliares;

import com.bufigol.utils.ConversorTemperatura;

import java.util.Objects;

public class Temperature {
    /**
     *  Identificador numérico del sensor de temperatura
     */
    private int id;

    /**
     * Temperatura en grados Celsius
     */
    private double tC;

    /**
     * Temperatura en grados Fahrenheit
     */
    private double tF;

    public Temperature(int id, double tC, double tF) {
        this.id = id;
        this.tC = tC;
        this.tF = tF;
    }

    public Temperature(int id) {
        this.id = id;
        this.tC = 0;
        this.tF = 0;
    }

    public Temperature(int id, double temp,char tempUnit) {
        this.id = id;
        if(tempUnit == 'C' || tempUnit == 'c' || tempUnit == 'F' || tempUnit == 'f'){
            if(tempUnit == 'C' || tempUnit == 'c'){
                this.tC = temp;
                this.tF= ConversorTemperatura.celsiusAFahrenheit(temp);
            }else {
                this.tC = ConversorTemperatura.fahrenheitACelsius(temp);
                this.tF = temp;
            }
        }else {
            this.tC = 0;
            this.tF = 0;
        }
    }

    public Temperature() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double gettC() {
        return tC;
    }

    public void settC(double tC) {
        this.tC = tC;
        this.tF = ConversorTemperatura.celsiusAFahrenheit(tC);
    }

    public double gettF() {
        return tF;
    }

    public void settF(double tF) {
        this.tF = tF;
        this.tC = ConversorTemperatura.fahrenheitACelsius(tF);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Temperature that)){
            return false;
        }else{
            return getId() == that.getId();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), gettC(), gettF());
    }

    @Override
    public String toString() {
        return "Temperature{" + "id=" + id +
                ", tC=" + tC +
                ", tF=" + tF +
                '}';
    }
}
