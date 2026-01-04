package com.tracker.app.controller;
import com.tracker.app.dto.OtpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.tracker.app.entity.User;
import com.tracker.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           RedirectAttributes redirect) {

        try {
            userService.register(user);

            redirect.addFlashAttribute("success",
                    "Account created! Please verify OTP sent to email");

            return "redirect:/verify-otp";

        } catch (Exception ex) {

            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/register";
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody OtpRequest request) {

        String response = userService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok(response);
    }

}


