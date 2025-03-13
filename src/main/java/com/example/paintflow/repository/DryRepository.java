package com.example.paintflow.repository;

import com.example.paintflow.entity.DryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DryRepository extends JpaRepository<DryEntity, LocalDateTime> {
    List<DryEntity> findByTimeBetween(LocalDateTime start, LocalDateTime end);
}
