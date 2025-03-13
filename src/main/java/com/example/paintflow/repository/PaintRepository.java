package com.example.paintflow.repository;

import com.example.paintflow.entity.PaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaintRepository extends JpaRepository<PaintEntity, LocalDateTime> {
    List<PaintEntity> findByTimeBetween(LocalDateTime start, LocalDateTime end);
}
