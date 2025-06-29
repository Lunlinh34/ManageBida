package com.example.Bida.Bida.Bida.Controller.Admin;

import com.example.Bida.Bida.Bida.Model.StoreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.io.IOException;
import com.example.Bida.Bida.Bida.Service.StoreService;

@Controller
public class StoreController extends RouterController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/store")
    public String showStoreList(Model model) {
        List<StoreEntity> stores = storeService.findAll();
        model.addAttribute("stores", stores);
        model.addAttribute("page", "Store/list");

        return "Admin/layout";
    }

    @GetMapping("/store/create")
    public String showCreateForm(Model model) {
        
        model.addAttribute("page", "Store/create");
        
        return "Admin/layout";
    }

    @PostMapping("/store/create")
    public String createStore(@ModelAttribute StoreEntity store, Model model,
            RedirectAttributes redirectAttributes, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        try {
            storeService.createStore(store, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Cửa hàng đã được tạo thành công!");
            return "redirect:/admin/store";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Admin/Store/create";
        }
    }

    @GetMapping("/store/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            StoreEntity store = storeService.findById(id);
            
            model.addAttribute("store", store);
            model.addAttribute("page", "Store/edit");
            
            return "Admin/layout";
        } catch (Exception e) {
            return "redirect:/admin/table";
        }
    }

    @PostMapping("/store/update")
    public String editStore(
            @ModelAttribute StoreEntity store,
            RedirectAttributes redirectAttributes) {

        try {
            storeService.updateStore(store.getId(), store);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật cửa hàng thành công!");
            return "redirect:/admin/store";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật cửa hàng: " + e.getMessage());
            return "redirect:/admin/store/edit/" + store.getId();
        }
    }

    @GetMapping("/store/delete/{id}")
    public String deleteStore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            storeService.deleteStore(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa cửa hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa cửa hàng: " + e.getMessage());
        }
        return "redirect:/admin/store";
    }
}