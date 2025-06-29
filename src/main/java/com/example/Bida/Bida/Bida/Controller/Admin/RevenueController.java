package com.example.Bida.Bida.Bida.Controller.Admin;

import com.example.Bida.Bida.Bida.Model.RevenueEntity;
import com.example.Bida.Bida.Bida.Service.RevenueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RevenueController extends RouterController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/revenue")
    public String showRevenueList(Model model) {
        List<RevenueEntity> revenues = revenueService.findAllTransactionsSuccess();
        model.addAttribute("revenues", revenues);
        model.addAttribute("page", "Revenue/list");
        return "Admin/layout";
    }
    @GetMapping("/revenue/filter")
    public String filterRevenue(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, Model model) {
        List<RevenueEntity> revenues = revenueService.findAllByCreatedAtBetween(startDate, endDate);
        model.addAttribute("revenues", revenues);
        model.addAttribute("page", "Revenue/list");
        return "Admin/layout";
    }
}