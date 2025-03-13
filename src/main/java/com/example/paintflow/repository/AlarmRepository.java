package com.example.paintflow.repository;

import com.example.paintflow.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {

    @Query("SELECT a FROM AlarmEntity a WHERE a.time BETWEEN :start AND :end")
    List<AlarmEntity> findAlarmsByTimeBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
