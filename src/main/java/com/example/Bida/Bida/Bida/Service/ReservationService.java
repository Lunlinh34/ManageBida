package com.example.Bida.Bida.Bida.Service;

import com.example.Bida.Bida.Bida.Enum.StatusOrder;
import com.example.Bida.Bida.Bida.Enum.StatusPay;
import com.example.Bida.Bida.Bida.Enum.StatusTable;
import com.example.Bida.Bida.Bida.Model.ReservationEntity;
import com.example.Bida.Bida.Bida.Model.TableEntity;
import com.example.Bida.Bida.Bida.Model.TransactionEntity;
import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Repository.ReservationRepository;
import com.example.Bida.Bida.Bida.Utils.StatusTableUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ReservationService extends BaseService<ReservationEntity, Long> {

    private final ReservationRepository reservationRepository;
    private final TableService tableService;
    private final TransactionService transactionService;

    Map<String, String> dataMap = new HashMap<>();
    public ReservationService(ReservationRepository reservationRepository, TableService tableService, TransactionService transactionService) {
        super(reservationRepository);
        this.reservationRepository = reservationRepository;
        this.tableService = tableService;
        this.transactionService = transactionService;
    }

    public Map<String, Object> getStatusData() {
        List<StatusOrder> statusList = Arrays.asList(StatusOrder.values());
        for (StatusOrder status : StatusOrder.values()) {
            dataMap.put(status.name(), StatusTableUtils.statusOrderMap.get(status));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("statusList", statusList);
        result.put("statusMap", dataMap);
        return result;
    }

    public void createReservation(ReservationEntity reservation) {
        TableEntity table = tableService.findById(reservation.getTable().getId()).orElse(null);
        if (table == null) {
            throw new IllegalArgumentException("Bàn không tồn tại");
        }
        if (table.getStatus() == StatusTable.INUSE) {
            throw new IllegalArgumentException("Bàn đã được sử dụng");
        }
        if (table.getStatus() == StatusTable.BOOKED) {
            throw new IllegalArgumentException("Bàn đã được đặt");
        }
        if (reservation.getStatus() == StatusOrder.PLAYED) {
            table.setStatus(StatusTable.INUSE);
        }
        if (reservation.getStatus() == StatusOrder.CANCEL) {
            table.setStatus(StatusTable.AVAILABLE);
        }
        if (reservation.getStatus() == StatusOrder.PENDING) {
            table.setStatus(StatusTable.BOOKED);
        }
        tableService.save(table);
        reservation.setTable(table);
        reservationRepository.save(reservation);
        if (reservation.getStatus() == StatusOrder.PLAYED) {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setReservation(reservation);
            transaction.setStartDate(reservation.getAppointmentTime());
            transaction.setStatus(StatusPay.PENDING);
            transactionService.createTransaction(transaction);
        }
    }

    public void updateReservation(Long id, ReservationEntity reservation) {
        ReservationEntity existingReservation = reservationRepository.findById(id).orElse(null);
        TableEntity table = tableService.findById(reservation.getTable().getId()).orElse(null);
        if (reservation.getStatus() == StatusOrder.PLAYED) {
            table.setStatus(StatusTable.INUSE);
        }
        if (reservation.getStatus() == StatusOrder.CANCEL) {
            TransactionEntity transaction = transactionService.findByReservation(existingReservation).orElse(null);
            if (transaction != null) {
                transactionService.deleteTransaction(transaction.getId());
            }
            table.setStatus(StatusTable.AVAILABLE);
        }
        if (reservation.getStatus() == StatusOrder.PENDING) {
            TransactionEntity transaction = transactionService.findByReservation(existingReservation).orElse(null);
            if (transaction != null) {
                transactionService.deleteTransaction(transaction.getId());
            }       
            table.setStatus(StatusTable.BOOKED);
        }

        if (existingReservation != null) {
            existingReservation.setUser(reservation.getUser());
            existingReservation.setTable(reservation.getTable());
            existingReservation.setStatus(reservation.getStatus());
            existingReservation.setAppointmentTime(reservation.getAppointmentTime());
            reservationRepository.save(existingReservation);
        }
        tableService.save(table);
        if (reservation.getStatus() == StatusOrder.PLAYED) {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setReservation(existingReservation);
            transaction.setStartDate(reservation.getAppointmentTime());
            transaction.setStatus(StatusPay.PENDING);
            transactionService.createTransaction(transaction);
        }
    }
    public void deleteReservation(Long id) {
        TableEntity table = tableService.findById(id).orElse(null);
        if (table != null) {
            table.setStatus(StatusTable.AVAILABLE);
            tableService.save(table);
        }
        reservationRepository.deleteById(id);
        transactionService.deleteTransaction(id);
    }

    public List<ReservationEntity> findByUserAndTable(UserEntity user, TableEntity table) {

        return reservationRepository.findByUserAndTable(user, table);
    }
    public List<ReservationEntity> findByStatus(StatusOrder status) {
        return reservationRepository.findByStatus(status);
    }
    public void updateReservation(Long id, StatusOrder status) {
        ReservationEntity reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            reservation.setStatus(status);
            reservationRepository.save(reservation);
        }
    }

    public Page<ReservationEntity> searchReservations(String keyword, Pageable pageable) {
        return reservationRepository.findByUser_FullnameContainingOrTable_NameContaining(keyword, keyword, pageable);
    }

    public Page<ReservationEntity> findByStatus(StatusOrder status, Pageable pageable) {
        return reservationRepository.findByStatus(status, pageable);
    }

    public Page<ReservationEntity> searchReservationsByStatus(String keyword, String status, Pageable pageable) {
        return reservationRepository.findByUser_FullnameContainingAndStatus(keyword, StatusOrder.valueOf(status), pageable);
    }
}
