package com.example.Bida.Bida.Bida.Service;
import com.example.Bida.Bida.Bida.Model.RevenueEntity;
import com.example.Bida.Bida.Bida.Repository.RevenueRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;


@Service
public class RevenueService extends BaseService<RevenueEntity, Long> {

    private final RevenueRepository revenueRepository;
    private final TableService tableService;

    Map<String, String> dataMap = new HashMap<>();
    public RevenueService(RevenueRepository revenueRepository, TableService tableService) {
        super(revenueRepository);
        this.revenueRepository = revenueRepository;
        this.tableService = tableService;
    }

    public void createRevenue(RevenueEntity revenue) {
        
        revenueRepository.save(revenue);
    }

    public void updateRevenue(Long id, RevenueEntity revenue) {
        RevenueEntity existingRevenue = revenueRepository.findById(id).orElse(null);
        if (existingRevenue != null) {
            existingRevenue.setTransaction(revenue.getTransaction());
            existingRevenue.setTotalPrice(revenue.getTotalPrice());
            revenueRepository.save(existingRevenue);
        }

    }
    public void deleteReservation(Long id) {

        revenueRepository.deleteById(id);
    }
    public List<RevenueEntity> findAllTransactionsSuccess() {
        return revenueRepository.findAll();
    }
    public List<RevenueEntity> findAllByCreatedAtBetween(String startDate, String endDate) {
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        
        return revenueRepository.findAllByCreatedAtBetween(start, end);
    }
    public List<RevenueEntity> findYear() {
        return revenueRepository.findAllByCreatedAtBetween(LocalDateTime.now().minusYears(1), LocalDateTime.now());
    }
    public List<RevenueEntity> findMonth() {
        return revenueRepository.findAllByCreatedAtBetween(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
    }
    public List<RevenueEntity> findWeek() {
        return revenueRepository.findAllByCreatedAtBetween(LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
    }

    public List<Map<String, Object>> getRevenueByMonths() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(11);
        
        List<RevenueEntity> revenues = revenueRepository.findAllByCreatedAtBetween(startDate, endDate);
        
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
        
        for (int i = 0; i < 12; i++) {
            LocalDateTime month = endDate.minusMonths(i);
            String monthKey = month.format(DateTimeFormatter.ofPattern("MM/yyyy"));
            monthlyRevenue.put(monthKey, 0.0);
        }
        
        for (RevenueEntity revenue : revenues) {
            String monthKey = revenue.getCreatedAt().format(DateTimeFormatter.ofPattern("MM/yyyy"));
            monthlyRevenue.compute(monthKey, (k, v) -> (v == null ? 0 : v) + revenue.getTotalPrice());
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        monthlyRevenue.forEach((month, total) -> {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("total", total);
            result.add(monthData);
        });
        
        return result;
    }
    public List<Map<String, Object>> getRevenueByDays() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(12);
        
        List<RevenueEntity> revenues = revenueRepository.findAllByCreatedAtBetween(startDate, endDate);
        
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
        
        for (int i = 0; i < 12; i++) {
            LocalDateTime month = endDate.minusDays(i);
            String monthKey = month.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            monthlyRevenue.put(monthKey, 0.0);
        }
        
        for (RevenueEntity revenue : revenues) {
            String monthKey = revenue.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            monthlyRevenue.compute(monthKey, (k, v) -> (v == null ? 0 : v) + revenue.getTotalPrice());
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        monthlyRevenue.forEach((month, total) -> {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("total", total);
            result.add(monthData);
        });
        
        return result;
    }
}
