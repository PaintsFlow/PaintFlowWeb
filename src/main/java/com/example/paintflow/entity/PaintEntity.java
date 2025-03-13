package com.example.paintflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "paint")
public class PaintEntity {

    @Id
    private LocalDateTime time;

    private double paintAmount;
    private double pressure;

    public PaintEntity() {}

    public PaintEntity(LocalDateTime time, double paintAmount, double pressure) {
        this.time = time;
        this.paintAmount = paintAmount;
        this.pressure = pressure;
    }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public double getPaintAmount() { return paintAmount; }
    public void setPaintAmount(double paintAmount) { this.paintAmount = paintAmount; }
    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }
}
