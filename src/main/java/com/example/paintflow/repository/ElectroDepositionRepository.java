package com.example.paintflow.repository;

import com.example.paintflow.entity.ElectroDepositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ElectroDepositionRepository extends JpaRepository<ElectroDepositionEntity, LocalDateTime> {
    List<ElectroDepositionEntity> findByTimeBetween(LocalDateTime start, LocalDateTime end);
}
