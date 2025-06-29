package com.example.Bida.Bida.Bida.Controller.Admin;

import com.example.Bida.Bida.Bida.Enum.StatusOrder;
import com.example.Bida.Bida.Bida.Model.ReservationEntity;
import com.example.Bida.Bida.Bida.Model.TransactionEntity;
import com.example.Bida.Bida.Bida.Service.ReservationService;
import com.example.Bida.Bida.Bida.Service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class TransactionController extends RouterController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/transaction")
    public String showTransactionList(Model model) {
        List<TransactionEntity> transactions = transactionService.findAll();
        Map<String, Object> statusData = transactionService.getStatusData();
        model.addAttribute("transactions", transactions);
        model.addAttribute("statusPayMap", statusData.get("statusPayMap"));
        model.addAttribute("page", "Transaction/list");
        return "Admin/layout";
    }

    @GetMapping("/transaction/create")
    public String showCreateForm(Model model) {
        Map<String, Object> statusData = transactionService.getStatusData();
        List<ReservationEntity> reservations = reservationService.findByStatus(StatusOrder.PLAYED);

        model.addAttribute("reservations", reservations);
        model.addAttribute("page", "Transaction/create");
        model.addAttribute("statusPayList", statusData.get("statusPayList"));
        model.addAttribute("statusPayMap", statusData.get("statusPayMap"));
        
        return "Admin/layout";
    }

    @PostMapping("/transaction/create")
    public String createTransaction(@ModelAttribute TransactionEntity transaction, RedirectAttributes redirectAttributes) {
        transactionService.createTransaction(transaction);
        redirectAttributes.addFlashAttribute("successMessage", "Giao dịch thành công!");
        return "redirect:/admin/transaction";
    }

    @GetMapping("/transaction/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            TransactionEntity transaction = transactionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt bàn với id: " + id));
            
            Map<String, Object> statusData = transactionService.getStatusData();
            List<ReservationEntity> reservations = reservationService.findAll();

            model.addAttribute("reservations", reservations);
            model.addAttribute("transaction", transaction);
            model.addAttribute("page", "Transaction/edit");
            model.addAttribute("statusPayList", statusData.get("statusPayList"));
            model.addAttribute("statusPayMap", statusData.get("statusPayMap"));
            
            return "Admin/layout";
        } catch (Exception e) {
            return "redirect:/admin/transaction";
        }
    }

    @PostMapping("/transaction/update")
    public String editTransaction(
                            @ModelAttribute TransactionEntity transaction,  
                            RedirectAttributes redirectAttributes) {

        try {
            transactionService.updateTransaction(transaction.getId(), transaction);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật giao dịch thành công!");
            return "redirect:/admin/transaction";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật giao dịch: " + e.getMessage());
            return "redirect:/admin/transaction/edit/" + transaction.getId();
        }
    }

    @GetMapping("/transaction/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            transactionService.deleteTransaction(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa giao dịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa giao dịch: " + e.getMessage());
        }
        return "redirect:/admin/transaction";
    }
}