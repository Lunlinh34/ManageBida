package com.example.Bida.Bida.Bida.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Service.ReservationService;
import com.example.Bida.Bida.Bida.Service.TableService;
import com.example.Bida.Bida.Bida.Service.UserService;
import com.example.Bida.Bida.Bida.Model.ReservationEntity;
import com.example.Bida.Bida.Bida.Model.TableEntity;
import com.example.Bida.Bida.Bida.Enum.StatusOrder;
import com.example.Bida.Bida.Bida.Enum.StatusTable;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping({"", "/"})
    public String getHome(@RequestParam(required = false) String search, 
                          @RequestParam(defaultValue = "0") int page, 
                          @RequestParam(defaultValue = "10") int size, 
                          Model model) {
        model.addAttribute("page", "list");

        Page<ReservationEntity> reservations;
        Map<String, Object> statusData = reservationService.getStatusData();

        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isEmpty()) {
            reservations = reservationService.searchReservations(search, pageable);
        } else {
            reservations = reservationService.findAll(pageable);
        }

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("statusMap", statusData.get("statusMap"));
        model.addAttribute("currentPage", reservations.getNumber());
        model.addAttribute("totalPages", reservations.getTotalPages());
        model.addAttribute("search", search);

        return "Booking/layout";
    }

    @GetMapping("/form/{id}")
    public String getFormBooking(@PathVariable("id") int id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByUsername(username).orElse(null);
        TableEntity table = tableService.findById(Long.valueOf(id)).orElse(null);
        

        model.addAttribute("page", "form");
        model.addAttribute("user", currentUser);
        model.addAttribute("table", table);

        return "Booking/layout";
    }

    @PostMapping("/create")
    public String postFormBooking(@RequestParam Map<String, String> allParams,
            RedirectAttributes redirectAttributes) {
        try {
 
            ReservationEntity reservation = new ReservationEntity();

            reservation.setAppointmentTime(LocalDateTime.parse(allParams.get("appointmentTime")));

            Long tableId = Long.valueOf(allParams.get("table"));
            Long userId = Long.valueOf(allParams.get("user"));

            TableEntity table = tableService.findById(tableId).orElse(null);
            UserEntity user = userService.findById(userId).orElse(null);


            reservation.setTable(table);
            reservation.setUser(user);
            reservation.setStatus(StatusOrder.PENDING);

            if (reservation.getAppointmentTime() == null || reservation.getTable() == null
                    || reservation.getUser() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Thông tin đặt bàn không hợp lệ. Vui lòng thử lại.");
                return "redirect:/home";
            }

            reservationService.createReservation(reservation);
            tableService.updateTableStatus(tableId, StatusTable.BOOKED);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt bàn thành công!");
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            return "redirect:/home";
        }
    }

    @GetMapping("/delete/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ReservationEntity reservation = reservationService.findById(id).orElse(null);   
            reservationService.updateReservation(id, StatusOrder.CANCEL);
            tableService.updateTableStatus(reservation.getTable().getId(), StatusTable.AVAILABLE);
            redirectAttributes.addFlashAttribute("successMessage", "Hủy đặt bàn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi hủy đặt bàn: " + e.getMessage());
        }
        return "redirect:/booking";
    }
}
