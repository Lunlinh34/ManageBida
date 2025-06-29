package com.example.Bida.Bida.Bida.Controller.Admin;

import com.example.Bida.Bida.Bida.Model.StoreEntity;
import com.example.Bida.Bida.Bida.Model.TableEntity;
import com.example.Bida.Bida.Bida.Service.StoreService;
import com.example.Bida.Bida.Bida.Service.TableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class TableController extends RouterController {

    @Autowired
    private TableService tableService;

    @Autowired
    private StoreService storeService;

    @GetMapping("/table")
    public String showTableList(Model model) {
        List<TableEntity> tables = tableService.findAll();
        Map<String, Object> statusData = tableService.getStatusData();
        Map<String, Object> categoryData = tableService.getCategoryData();
        model.addAttribute("tables", tables);
        model.addAttribute("page", "Table/list");
        model.addAttribute("statusMap", statusData.get("statusMap"));
        model.addAttribute("categoryMap", categoryData.get("categoryMap"));

        return "Admin/layout";
    }

    @GetMapping("/table/create")
    public String showCreateForm(Model model) {
        Map<String, Object> statusData = tableService.getStatusData();
        Map<String, Object> categoryData = tableService.getCategoryData();
        List<StoreEntity> stores = storeService.findAll();
        
        model.addAttribute("stores", stores);
        model.addAttribute("page", "Table/create");
        model.addAttribute("statusList", statusData.get("statusList"));
        model.addAttribute("statusMap", statusData.get("statusMap"));
        model.addAttribute("categoryList", categoryData.get("categoryList")); 
        model.addAttribute("categoryMap", categoryData.get("categoryMap"));
        
        return "Admin/layout";
    }

    @PostMapping("/table/create")
    public String createTable(@ModelAttribute TableEntity table,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return showCreateForm(model);
        }

        try {
            tableService.createTable(table, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Bàn Bi-A đã được tạo thành công!");
            return "redirect:/admin/table";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/table/create";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi tải ảnh. Vui lòng thử lại.");
            return "redirect:/admin/table/create";
        }
    }

    @GetMapping("/table/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            TableEntity table = tableService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với id: " + id));
            
            Map<String, Object> statusData = tableService.getStatusData();
            Map<String, Object> categoryData = tableService.getCategoryData();
            List<StoreEntity> stores = storeService.findAll();
        
            model.addAttribute("stores", stores);
            model.addAttribute("table", table);
            model.addAttribute("page", "Table/edit");
            model.addAttribute("statusList", statusData.get("statusList"));
            model.addAttribute("statusMap", statusData.get("statusMap"));
            model.addAttribute("categoryList", categoryData.get("categoryList"));
            model.addAttribute("categoryMap", categoryData.get("categoryMap"));
            
            return "Admin/layout";
        } catch (Exception e) {
            return "redirect:/admin/table";
        }
    }

    @PostMapping("/table/edit/{id}")
    public String editTable(@PathVariable Long id,
                            @ModelAttribute TableEntity table,
                            @RequestParam(required = false) MultipartFile imageFile,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/admin/table/edit/" + id;
        }

        try {
            tableService.updateTable(id, table, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật bàn thành công!");
            return "redirect:/admin/table";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật bàn: Bạn chỉ có thể chỉnh sửa bàn!");
            return "redirect:/admin/table/edit/" + id;
        }
    }

    @GetMapping("/table/delete/{id}")
    public String deleteTable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tableService.deleteTable(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa bàn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa bàn: Bạn chỉ có thể chỉnh sửa bàn!");
        }
        return "redirect:/admin/table";
    }
}