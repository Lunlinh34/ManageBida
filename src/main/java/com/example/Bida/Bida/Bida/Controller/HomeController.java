package com.example.Bida.Bida.Bida.Controller;

import com.example.Bida.Bida.Bida.Service.ReviewService;
import com.example.Bida.Bida.Bida.Service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private TableService tableService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping({"/home", "/"})
    public String getList(Model model) {
        

        
        model.addAttribute("page", "/table/list");
        model.addAttribute("tablesPoor", tableService.TablePoor8());
        model.addAttribute("tablesCarom", tableService.TableCarom8());
        model.addAttribute("reviews", reviewService.getTop10Reviews());
        
        return "home";
    }

    @GetMapping({"/tables/poor", "/tables/poor/"})
    public String getListPoor(Model model) {
        model.addAttribute("page", "/table/poor");
        model.addAttribute("tablesPoor", tableService.TablePoor());
        return "User/Table/poor";
    }

    @GetMapping({"/tables/carom", "/tables/carom/"})
    public String getListCarom(Model model) {
        model.addAttribute("page", "/table/carom");
        model.addAttribute("tablesCarom", tableService.TableCarom());
        return "User/Table/carom";
    }

    @PostMapping("/tables/search")
    public String searchTables(@RequestParam("search") String search, Model model) {
        model.addAttribute("tablesPoor", tableService.searchTablesPoorCommon(search));
        return "User/Table/poor :: #tableList";
    }
    @PostMapping("/tables/search/carom")
    public String searchTablesCarom(@RequestParam("search") String search, Model model) {
        model.addAttribute("tablesCarom", tableService.searchTablesCaromCommon(search));
        return "User/Table/carom :: #tableList";
    }
}