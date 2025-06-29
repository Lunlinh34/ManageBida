package com.example.Bida.Bida.Bida.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Security.JwtUtil;
import com.example.Bida.Bida.Bida.Service.UserService;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<UserEntity> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {

            logger.debug("Password matches: {}", passwordEncoder.matches(password, userOpt.get().getPassword()));  // In ra kết quả kiểm tra mật khẩu
            System.out.println("Password matches: {}" + passwordEncoder.matches(password, userOpt.get().getPassword()));  // In ra kết quả kiểm tra mật khẩu

        }

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String token = jwtUtil.generateToken(userOpt.get().getUsername());
            model.addAttribute("token", token);
            return "home";
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, 
                        @RequestParam String password,
                        @RequestParam String fullname,
                        @RequestParam String email,
                        @RequestParam String phoneNumber,
                        Model model) {
        try {
            if (userService.findByUsername(username).isPresent()) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            userService.registerUser(username, passwordEncoder.encode(password), fullname, email, phoneNumber);
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register"; 
        }
    }


    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
