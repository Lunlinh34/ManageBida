package com.example.Bida.Bida.Bida.Service;

import com.example.Bida.Bida.Bida.Enum.StatusOrder;
import com.example.Bida.Bida.Bida.Enum.StatusPay;
import com.example.Bida.Bida.Bida.Enum.StatusTable;
import com.example.Bida.Bida.Bida.Model.ReservationEntity;
import com.example.Bida.Bida.Bida.Model.RevenueEntity;
import com.example.Bida.Bida.Bida.Model.TableEntity;
import com.example.Bida.Bida.Bida.Model.TransactionEntity;
import com.example.Bida.Bida.Bida.Repository.TransactionRepository;
import com.example.Bida.Bida.Bida.Utils.StatusTableUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;


@Service
@Lazy
public class TransactionService extends BaseService<TransactionEntity, Long> {

    private final TransactionRepository transactionRepository;
    private final ReservationService reservationService;
    private final TableService tableService;
    private final RevenueService revenueService;
    Map<String, String> statusPayMap = new HashMap<>();

    public TransactionService(TransactionRepository transactionRepository, @Lazy ReservationService reservationService, TableService tableService, RevenueService revenueService) {
        super(transactionRepository);
        this.transactionRepository = transactionRepository;
        this.reservationService = reservationService;
        this.tableService = tableService;
        this.revenueService = revenueService;
    }

    public Map<String, Object> getStatusData() {
        List<StatusPay> statusPayList = Arrays.asList(StatusPay.values());
        for (StatusPay statusPay : statusPayList) {
            statusPayMap.put(statusPay.name(), StatusTableUtils.statusPayMap.get(statusPay));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("statusPayList", statusPayList);
        result.put("statusPayMap", statusPayMap);
        return result;
    }

    @SuppressWarnings("unused")
    public void createTransaction(TransactionEntity transaction) {
        ReservationEntity reservation = reservationService.findById(transaction.getReservation().getId()).orElse(null);
        TableEntity table = tableService.findById(reservation.getTable().getId()).orElse(null);
        if (reservation == null) {
            throw new IllegalArgumentException("Đặt bàn không tồn tại");
        }
        
        if(transaction.getStatus() == StatusPay.PENDING) {
            table.setStatus(StatusTable.BOOKED);
        }
        if(transaction.getStatus() == StatusPay.FAIL) {
            reservation.setStatus(StatusOrder.CANCEL);
            table.setStatus(StatusTable.AVAILABLE);
        }
        tableService.save(table);
        transactionRepository.save(transaction);
        reservationService.save(reservation);
        if(transaction.getStatus() == StatusPay.SUCCESS) {
            reservation.setStatus(StatusOrder.CANCEL);
            table.setStatus(StatusTable.AVAILABLE);
            RevenueEntity revenue = new RevenueEntity();
            revenue.setTransaction(transaction);
            System.out.println("reservation.getTable().getHourlyRate() : " + reservation.getTable().getHourlyRate());
            System.out.println("transaction.getEndDate().getMinute() - transaction.getStartDate().getMinute() : " + (transaction.getEndDate().getMinute() - transaction.getStartDate().getMinute()));
            revenue.setTotalPrice(reservation.getTable().getHourlyRate()/60 * (transaction.getEndDate().getMinute() - transaction.getStartDate().getMinute()));
            revenueService.save(revenue);
        }
    }

    @SuppressWarnings("unused")
    public void updateTransaction(Long id, TransactionEntity transaction) {
        TransactionEntity existingTransaction = transactionRepository.findById(id).orElse(null);
        ReservationEntity reservation = reservationService.findById(transaction.getReservation().getId()).orElse(null);
        TableEntity table = tableService.findById(reservation.getTable().getId()).orElse(null);
        
        if (reservation == null) {
            throw new IllegalArgumentException("Đặt bàn không tồn tại");
        }
        if(transaction.getStatus() == StatusPay.SUCCESS) {
            reservation.setStatus(StatusOrder.COMPLETED);
            table.setStatus(StatusTable.AVAILABLE);
        }
        if(transaction.getStatus() == StatusPay.PENDING) {
            reservation.setStatus(StatusOrder.PENDING);
            table.setStatus(StatusTable.BOOKED);
        }
        if(transaction.getStatus() == StatusPay.FAIL) {
            reservation.setStatus(StatusOrder.CANCEL);
            table.setStatus(StatusTable.AVAILABLE);
        }
        if (existingTransaction != null) {
            existingTransaction.setReservation(transaction.getReservation());
            existingTransaction.setStartDate(transaction.getStartDate());
            existingTransaction.setEndDate(transaction.getEndDate());
            existingTransaction.setStatus(transaction.getStatus());
            
            transactionRepository.save(existingTransaction);
        }
        if(transaction.getStatus() == StatusPay.SUCCESS) {
            reservation.setStatus(StatusOrder.COMPLETED);
            table.setStatus(StatusTable.AVAILABLE);
            RevenueEntity revenue = new RevenueEntity();
            revenue.setTransaction(transaction);
            System.out.println("reservation.getTable().getHourlyRate() : " + reservation.getTable().getHourlyRate());
            System.out.println("Duration.between(transaction.getStartDate(), transaction.getEndDate()).toMinutes() : " + Duration.between(transaction.getStartDate(), transaction.getEndDate()).toMinutes());
            revenue.setTotalPrice(reservation.getTable().getHourlyRate()/60 * Duration.between(transaction.getStartDate(), transaction.getEndDate()).toMinutes());
            revenueService.save(revenue);
        }
    }
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public Optional<TransactionEntity> findByReservation(ReservationEntity reservation) {
        return transactionRepository.findByReservation(reservation);
    }
}
