package com.example.Bida.Bida.Bida.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

import com.example.Bida.Bida.Bida.Model.ReviewEntity;
import com.example.Bida.Bida.Bida.Model.StoreEntity;
import com.example.Bida.Bida.Bida.Service.StoreService; 
import com.example.Bida.Bida.Bida.Service.ReviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Service.UserService;


@Controller
@RequestMapping("/store")
public class StoreClientController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping({"/", ""})
    public String getStore(Model model) {

        UserEntity userCurrent = userService.getCurrentUser();
        List<StoreEntity> stores = storeService.findAll();
        model.addAttribute("stores", stores);
        model.addAttribute("page", "/store");
        model.addAttribute("userCurrent", userCurrent);

        return "Store/index";
    }

    @PostMapping("/search")
    public String searchStores(@RequestParam("search") String search, Model model) {
        List<StoreEntity> stores = storeService.searchStores(search);
        model.addAttribute("stores", stores);
        return "Store/index";
          }

    @GetMapping("/detail/{id}")
    public String getReviewDetail(@PathVariable Long id, Model model) {
        StoreEntity store = storeService.findById(id);
        List<ReviewEntity> reviews = reviewService.findByStoreId(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("store", store);
        return "Store/detail";
    }

    @GetMapping("/{id}/reviews")
    public String getReviews(@PathVariable Long id, Model model) {
        List<ReviewEntity> reviews = reviewService.findByStoreId(id);
        model.addAttribute("reviews", reviews);
        return "Store/reviews";
    }   

    @PostMapping("/{id}/reviews")
    public String createReview(@PathVariable Long id, @RequestParam("userId") Long userId, @RequestParam("rating") int rating, @RequestParam("comment") String comment, Model model) {
        ReviewEntity review = new ReviewEntity();
        review.setStore(storeService.findById(id));
        review.setUser(userService.findById(userId).orElse(null));
        review.setRating(rating);
        review.setComment(comment);
        reviewService.save(review);
        return "redirect:/store/detail/{id}";
    }
}
