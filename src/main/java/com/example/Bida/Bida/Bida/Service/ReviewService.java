package com.example.Bida.Bida.Bida.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bida.Bida.Bida.Model.ReviewEntity;
import com.example.Bida.Bida.Bida.Repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired  
    private ReviewRepository reviewRepository;

    public List<ReviewEntity> findAll() {
        return reviewRepository.findAll();
    }

    public ReviewEntity save(ReviewEntity review) {
        return reviewRepository.save(review);
    }

    public ReviewEntity findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<ReviewEntity> findByStoreId(Long storeId) {
        return reviewRepository.findByStoreId(storeId);
    }   

    public List<ReviewEntity> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<ReviewEntity> findByStoreIdAndUserId(Long storeId, Long userId) {
        return reviewRepository.findByStoreIdAndUserId(storeId, userId);
    }       

    public ReviewEntity updateReview(Long id, ReviewEntity review) {
        ReviewEntity existingReview = reviewRepository.findById(id).orElse(null);
        if (existingReview != null) {
            existingReview.setUser(review.getUser());
            existingReview.setStore(review.getStore());
            existingReview.setRating(review.getRating());
            existingReview.setComment(review.getComment());
            return reviewRepository.save(existingReview);
        }
        return null;
    }

    public List<ReviewEntity> findAll(Long storeId, Integer rating) {
        if (storeId != null && rating != null) {
            return reviewRepository.findByStoreIdAndRating(storeId, rating);
        } else if (storeId != null) {
            return reviewRepository.findByStoreId(storeId);
        } else if (rating != null) {
            return reviewRepository.findByRating(rating);
        }
        return reviewRepository.findAll();
    }

    public List<ReviewEntity> getTop10Reviews() {
        return reviewRepository.findTop10ByOrderByRatingDesc();
    }

    public List<ReviewEntity> findByStoreIdAndRating(Long storeId, Integer rating) {
        return reviewRepository.findByStoreIdAndRating(storeId, rating);
    }
}