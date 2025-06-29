package com.example.Bida.Bida.Bida.Controller.Admin;

import com.example.Bida.Bida.Bida.Enum.StatusOrder;
import com.example.Bida.Bida.Bida.Enum.StatusTable;
import com.example.Bida.Bida.Bida.Model.ReservationEntity;
import com.example.Bida.Bida.Bida.Model.TableEntity;
import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Service.ReservationService;
import com.example.Bida.Bida.Bida.Service.TableService;
import com.example.Bida.Bida.Bida.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class ReservationController extends RouterController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TableService tableService;

    @GetMapping("/reservation")
    public String showReservationList(@RequestParam(required = false) String search, 
                                   @RequestParam(required = false) String status, 
                                   @RequestParam(defaultValue = "0") int page, 
                                   @RequestParam(defaultValue = "10") int size, 
                                   Model model) {
        Map<String, Object> statusData = reservationService.getStatusData();
        @SuppressWarnings("unchecked")
        List<StatusOrder> statusList = (List<StatusOrder>) statusData.get("statusList");
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationEntity> reservations;

        if (search != null && !search.isEmpty() && status != null && !status.isEmpty()) {
            reservations = reservationService.searchReservationsByStatus(search, status, pageable);
        } else if (search != null && !search.isEmpty()) {
            reservations = reservationService.searchReservations(search, pageable);
        } else if (status != null && !status.isEmpty()) {
            reservations = reservationService.findByStatus(StatusOrder.valueOf(status), pageable);
        } else {
            reservations = reservationService.findAll(pageable);
        }

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("page", "Reservation/list");
        model.addAttribute("statusMap", statusData.get("statusMap"));
        model.addAttribute("currentPage", reservations.getNumber());
        model.addAttribute("totalPages", reservations.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        model.addAttribute("statusList", statusList);

        return "Admin/layout";
    }

    @GetMapping("/reservation/create")
    public String showCreateForm(Model model) {
        Map<String, Object> statusData = reservationService.getStatusData();
        List<UserEntity> users = userService.findUserByRoleMember();
        List<TableEntity> tables = tableService.findAll();

        model.addAttribute("users", users);
        model.addAttribute("tables", tables);
        model.addAttribute("page", "Reservation/create");
        model.addAttribute("statusList", statusData.get("statusList"));
        model.addAttribute("statusMap", statusData.get("statusMap"));

        return "Admin/layout";
    }

    @PostMapping("/reservation/create")
    public String createReservation(@ModelAttribute ReservationEntity reservation, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            reservationService.createReservation(reservation);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt bàn thành công!");
            return "redirect:/admin/reservation";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/reservation/create";
        }
    }

    @GetMapping("/reservation/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            ReservationEntity reservation = reservationService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt bàn với id: " + id));

            Map<String, Object> statusData = reservationService.getStatusData();
            List<UserEntity> users = userService.findUserByRoleMember();
            List<TableEntity> tables = tableService.findAll();

            model.addAttribute("reservation", reservation);
            model.addAttribute("page", "Reservation/edit");
            model.addAttribute("users", users);
            model.addAttribute("tables", tables);
            model.addAttribute("statusList", statusData.get("statusList"));
            model.addAttribute("statusMap", statusData.get("statusMap"));

            return "Admin/layout";
        } catch (Exception e) {
            return "redirect:/admin/reservation";
        }
    }

    @PostMapping("/reservation/update")
    public String editReservation(
            @ModelAttribute ReservationEntity reservation,
            RedirectAttributes redirectAttributes) {

        try {
            reservationService.updateReservation(reservation.getId(), reservation);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật đặt bàn thành công!");
            return "redirect:/admin/reservation";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật đặt bàn: Bạn chỉ có thể cập nhật đặt bàn khi đã thanh toán!");
            return "redirect:/admin/reservation/edit/" + reservation.getId();
        }
    }

    @GetMapping("/reservation/delete/{id}")
    public String deleteTable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ReservationEntity reservation = reservationService.findById(id).orElse(null);
            TableEntity table = tableService.findById(reservation.getTable().getId()).orElse(null);
            table.setStatus(StatusTable.AVAILABLE);
            reservationService.deleteReservation(id);
            tableService.save(table);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa đặt bàn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đặt bàn: Bạn chỉ có thể xóa đặt bàn khi đã thanh toán!");
        }
        return "redirect:/admin/reservation";
    }
}