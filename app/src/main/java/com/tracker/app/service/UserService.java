package com.tracker.app.service;

import com.tracker.app.entity.User;
import com.tracker.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // mark user not verified
        user.setVerified(false);

        // generate 6-digit OTP
        String otpCode = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setOtp(otpCode);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        // send OTP mail
        emailService.sendOTP(user.getEmail(), otpCode);

        // save user
        return userRepository.save(user);
    }


    // VERIFY OTP
    public String verifyOtp(String email, String otp) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User not found";
        }

        if (user.get().isVerified()) {
            return "User already verified";
        }

        if (user.get().getOtp() == null) {
            return "OTP not generated";
        }

        if (!user.get().getOtp().equals(otp)) {
            return "Invalid OTP";
        }

        if (user.get().getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP expired";
        }

        // SUCCESS — mark verified
        user.get().setVerified(true);
        user.get().setOtp(null);
        user.get().setOtpExpiry(null);

        userRepository.save(user.get());

        return "Verification successful";
    }
}


