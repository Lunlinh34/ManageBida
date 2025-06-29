    package com.example.Bida.Bida.Bida.Controller.Admin;


    import java.util.List;
    import java.util.Map;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    import com.example.Bida.Bida.Bida.Service.RevenueService;

    @Controller
    public class DashboardController extends RouterController {
        @Autowired
        private RevenueService revenueService;
        @GetMapping({"/", "", "/dashboard"})
        public String dashboard(Model model, @RequestParam(required = false) String view) {
            if(view == null) {
                view = "month";
            }
            if(view.equals("day")) {
                List<Map<String, Object>> monthlyRevenue = revenueService.getRevenueByDays();
                System.out.println("dailyRevenue: " + monthlyRevenue);
                model.addAttribute("page", "Dashboard/list");
                model.addAttribute("monthlyRevenue", monthlyRevenue);
                return "Admin/layout";
            }
            List<Map<String, Object>> monthlyRevenue = revenueService.getRevenueByMonths();
                System.out.println("monthlyRevenue: " + monthlyRevenue);
                model.addAttribute("page", "Dashboard/list");
                model.addAttribute("monthlyRevenue", monthlyRevenue);
            return "Admin/layout";
        }
    }
