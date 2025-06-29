package com.example.Bida.Bida.Bida.Repository;
import com.example.Bida.Bida.Bida.Model.RevenueEntity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RevenueRepository extends JpaRepository<RevenueEntity, Long> {
    
    @Query("SELECT r FROM RevenueEntity r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<RevenueEntity> findAllByCreatedAtBetween(LocalDateTime startDate,LocalDateTime endDate);
}
