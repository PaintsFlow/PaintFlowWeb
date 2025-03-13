package com.example.paintflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alarm")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alarmId;

    private LocalDateTime time;
    private String sensor;
    private double data;

    @Column(length = 500)
    private String message;

    public AlarmEntity() {}

    public AlarmEntity(LocalDateTime time, String sensor, double data, String message) {
        this.time = time;
        this.sensor = sensor;
        this.data = data;
        this.message = message;
    }

    public int getAlarmId() { return alarmId; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public String getSensor() { return sensor; }
    public void setSensor(String sensor) { this.sensor = sensor; }
    public double getData() { return data; }
    public void setData(double data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
