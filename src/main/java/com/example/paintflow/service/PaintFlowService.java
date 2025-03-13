package com.example.paintflow.service;

import org.springframework.stereotype.Service;
import com.example.paintflow.repository.AlarmRepository;
import com.example.paintflow.repository.DryRepository;
import com.example.paintflow.repository.ElectroDepositionRepository;
import com.example.paintflow.repository.PaintRepository;
import com.example.paintflow.entity.AlarmEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaintFlowService {
    private final ElectroDepositionRepository electroDepositionRepository;
    private final DryRepository dryRepository;
    private final PaintRepository paintRepository;
    private final AlarmRepository alarmRepository;

    public PaintFlowService(ElectroDepositionRepository electroDepositionRepository,
                            DryRepository dryRepository,
                            PaintRepository paintRepository,
                            AlarmRepository alarmRepository) {
        this.electroDepositionRepository = electroDepositionRepository;
        this.dryRepository = dryRepository;
        this.paintRepository = paintRepository;
        this.alarmRepository = alarmRepository;
    }

    public List<?> getDataByProcess(String process, LocalDateTime start, LocalDateTime end) {
        switch (process) {
            case "electroDeposition":
                return electroDepositionRepository.findByTimeBetween(start, end);
            case "dry":
                return dryRepository.findByTimeBetween(start, end);
            case "paint":
                return paintRepository.findByTimeBetween(start, end);
            default:
                throw new IllegalArgumentException("잘못된 공정명: " + process);
        }
    }

    public List<AlarmEntity> getAlarmDataByProcess(String process, LocalDateTime start, LocalDateTime end) {
        List<AlarmEntity> allAlarms = alarmRepository.findAlarmsByTimeBetween(start, end);

        return allAlarms.stream()
                .filter(alarm -> {
                    String sensor = alarm.getSensor();
                    switch (process) {
                        case "electroDeposition":
                            return sensor.contains("PH") || sensor.contains("전류");
                        case "dry":
                            return sensor.contains("온도") || sensor.contains("습도");
                        case "paint":
                            return sensor.contains("페인트") || sensor.contains("스프레이");
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
