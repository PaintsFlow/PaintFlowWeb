package com.example.paintflow.controller;

import org.springframework.web.bind.annotation.*;

import com.example.paintflow.entity.AlarmEntity;
import com.example.paintflow.service.PaintFlowService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaintFlowController {

    private final PaintFlowService paintFlowService;

    public PaintFlowController(PaintFlowService paintFlowService) {
        this.paintFlowService = paintFlowService;
    }

    @GetMapping("/{process}/{startDate}/{endDate}")
    public List<?> getData(
            @PathVariable String process,
            @PathVariable String startDate,
            @PathVariable String endDate) {
        
        // DateTime 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        return paintFlowService.getDataByProcess(process, start, end);
    }
    
    @GetMapping("/download/{process}/{start}/{end}")
    public void downloadData(
            @PathVariable String process,
            @PathVariable String start,
            @PathVariable String end,
            HttpServletResponse response) throws IOException {
        
        System.out.println("다운로드 요청: " + process + " " + start + " ~ " + end);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        List<?> dataList = paintFlowService.getDataByProcess(process, startDate, endDate);

        if (dataList.isEmpty()) {
            System.out.println("데이터 없음");
            return;
        }

        // 파일 다운로드 설정
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + process + "_data.csv");

        // CSV 파일 생성
        try (PrintWriter writer = response.getWriter()) {
            Object firstItem = dataList.get(0);
            Field[] fields = firstItem.getClass().getDeclaredFields();

            // 컬럼명 출력
            writer.println(Arrays.stream(fields)
                    .map(Field::getName)
                    .collect(Collectors.joining(",")));

            // 데이터 출력
            for (Object item : dataList) {
                writer.println(Arrays.stream(fields)
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                return String.valueOf(field.get(item));
                            } catch (IllegalAccessException e) {
                                return "";
                            }
                        })
                        .collect(Collectors.joining(",")));
            }
        }
    }
    
    @GetMapping("/alarm/{process}/{start}/{end}")
    public List<AlarmEntity> getAlarmData(
            @PathVariable String process,
            @PathVariable String start,
            @PathVariable String end) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        return paintFlowService.getAlarmDataByProcess(process, startDate, endDate);
    }
}