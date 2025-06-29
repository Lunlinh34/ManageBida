package com.example.Bida.Bida.Bida.Repository;

import com.example.Bida.Bida.Bida.Enum.StatusOrder;
import com.example.Bida.Bida.Bida.Model.ReservationEntity;
import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Model.TableEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByUserAndTable(UserEntity user, TableEntity table);
    List<ReservationEntity> findByStatus(StatusOrder status);
    List<ReservationEntity> findByUser_FullnameContainingOrTable_NameContaining(String userFullname, String tableName);
    Page<ReservationEntity> findByUser_FullnameContainingOrTable_NameContaining(String userFullname, String tableName, Pageable pageable);
    Page<ReservationEntity> findByUser_FullnameContainingAndStatus(String userFullname, StatusOrder status, Pageable pageable);
    Page<ReservationEntity> findByStatus(StatusOrder status, Pageable pageable);
}
