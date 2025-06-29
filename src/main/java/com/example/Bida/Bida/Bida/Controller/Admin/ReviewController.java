package com.example.Bida.Bida.Bida.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Bida.Bida.Bida.Model.ReviewEntity;
import com.example.Bida.Bida.Bida.Service.ReviewService;
import com.example.Bida.Bida.Bida.Service.UserService;
import com.example.Bida.Bida.Bida.Service.StoreService;

import java.util.List;

import javax.validation.Valid;

@Controller
public class ReviewController extends RouterController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @GetMapping({ "/review", "/review/" })
    public String listReviews(@RequestParam(required = false) Long storeId, 
                              @RequestParam(required = false) Integer rating, 
                              Model model) {
        List<ReviewEntity> reviews = reviewService.findAll(storeId, rating);        
        model.addAttribute("reviews", reviews);
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("page", "Review/list");
        return "Admin/layout";
    }

    @GetMapping("/review/create")
    public String newReview(Model model) {
        model.addAttribute("review", new ReviewEntity());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("stores", storeService.findAll());

        model.addAttribute("page", "Review/create");
        return "Admin/layout";
    }

    @PostMapping("/review/save")
    public String saveReview(@Valid ReviewEntity review, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/error404";
        }
        reviewService.save(review);
        redirectAttributes.addFlashAttribute("success", "Review created successfully");
        return "redirect:/admin/review";
    }

    @PostMapping("/review/update")
    public String editReview(
            @ModelAttribute ReviewEntity review,
            RedirectAttributes redirectAttributes) {
        try {
            reviewService.updateReview(review.getId(), review);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật đánh giá thành công!");
            return "redirect:/admin/review";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật cửa hàng: " + e.getMessage());
            return "redirect:/error404";
        }
    }

    @GetMapping("/review/{id}/edit")
    public String editReview(@PathVariable Long id, Model model) {
        ReviewEntity review = reviewService.findById(id);
        model.addAttribute("review", review);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("page", "Review/edit");
        return "Admin/layout";
    }

    @GetMapping("/review/{id}/delete")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Review deleted successfully");
        return "redirect:/admin/review";
    }

}
