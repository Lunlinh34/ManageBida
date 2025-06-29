package com.example.Bida.Bida.Bida.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bida.Bida.Bida.Model.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByStoreId(Long storeId);
    List<ReviewEntity> findByUserId(Long userId);
    List<ReviewEntity> findByStoreIdAndUserId(Long storeId, Long userId);
    List<ReviewEntity> findByStoreIdOrderByCreatedAtDesc(Long storeId);
    List<ReviewEntity> findByStoreIdOrderByRatingDesc(Long storeId);
    List<ReviewEntity> findByRating(Integer rating);
    List<ReviewEntity> findByStoreIdAndRating(Long storeId, Integer rating);
    List<ReviewEntity> findTop10ByOrderByRatingDesc();
}
