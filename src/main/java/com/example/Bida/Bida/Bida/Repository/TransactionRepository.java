package com.example.Bida.Bida.Bida.Repository;

import java.util.Optional;

import com.example.Bida.Bida.Bida.Model.ReservationEntity;
import com.example.Bida.Bida.Bida.Model.TransactionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{
    Optional<TransactionEntity> findByReservation(ReservationEntity reservation);
}
