package com.example.Bida.Bida.Bida.Repository;

import java.util.List;

import com.example.Bida.Bida.Bida.Model.StoreEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    List<StoreEntity> findByNameContainingIgnoreCase(String search);
    boolean existsByName(String name);
}
