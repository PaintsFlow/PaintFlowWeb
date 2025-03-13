package com.example.paintflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dry")
public class DryEntity {

    @Id
    private LocalDateTime time;

    private double temperature;
    private double humidity;

    public DryEntity() {}

    public DryEntity(LocalDateTime time, double temperature, double humidity) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }
}
