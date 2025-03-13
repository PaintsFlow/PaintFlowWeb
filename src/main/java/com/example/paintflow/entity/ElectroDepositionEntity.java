package com.example.paintflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "electroDeposition")
public class ElectroDepositionEntity {

    @Id
    private LocalDateTime time;

    private double waterLevel;
    private double viscosity;
    private double ph;
    private double current;
    private double voltage;

    public ElectroDepositionEntity() {}

    public ElectroDepositionEntity(LocalDateTime time, double waterLevel, double viscosity, double ph, double current, double voltage) {
        this.time = time;
        this.waterLevel = waterLevel;
        this.viscosity = viscosity;
        this.ph = ph;
        this.current = current;
        this.voltage = voltage;
    }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(double waterLevel) { this.waterLevel = waterLevel; }
    public double getViscosity() { return viscosity; }
    public void setViscosity(double viscosity) { this.viscosity = viscosity; }
    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }
    public double getCurrent() { return current; }
    public void setCurrent(double current) { this.current = current; }
    public double getVoltage() { return voltage; }
    public void setVoltage(double voltage) { this.voltage = voltage; }
}
