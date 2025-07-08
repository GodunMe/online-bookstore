package fa.training.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fa.training.model.User;
import fa.training.service.EmailService;
import fa.training.service.UserService;
import fa.training.util.Encryption;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password/send-otp")
    @ResponseBody
    public ResponseEntity<String> sendOtp(@RequestParam String email, HttpSession session) {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Email not found.");
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        session.setAttribute("forgotOtp", otp);
        session.setAttribute("forgotEmail", email);
        session.setAttribute("forgotOtpExpiry", System.currentTimeMillis() + 5 * 60 * 1000);
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok("OTP sent to your email.");
    }


    @PostMapping("/forgot-password/verify-otp")
    @ResponseBody
    public ResponseEntity<String> verifyOtp(@RequestParam String otp, @RequestParam String email, HttpSession session) {
        String sessionOtp = (String) session.getAttribute("forgotOtp");
        Long expiry = (Long) session.getAttribute("forgotOtpExpiry");
        String storedEmail = (String) session.getAttribute("forgotEmail");

        if (sessionOtp == null || expiry == null || !email.equals(storedEmail)) {
            return ResponseEntity.badRequest().body("Invalid OTP session.");
        }

        if (System.currentTimeMillis() > expiry) {
            return ResponseEntity.badRequest().body("OTP expired.");
        }

        if (!sessionOtp.equals(otp)) {
            return ResponseEntity.badRequest().body("Incorrect OTP.");
        }

        session.setAttribute("forgotOtpVerified", true);
        return ResponseEntity.ok("OTP is valid.");
    }

    @PostMapping("/forgot-password/reset")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload, HttpSession session) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");

        Boolean verified = (Boolean) session.getAttribute("forgotOtpVerified");
        String storedEmail = (String) session.getAttribute("forgotEmail");

        if (verified == null || !verified || !email.equals(storedEmail)) {
            return ResponseEntity.badRequest().body("OTP not verified.");
        }

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = userOptional.get();
        String hashedNewPass = Encryption.sha256(newPassword);

        if (hashedNewPass.equals(user.getPassword())) {
            return ResponseEntity.badRequest().body("New password must be different from current password.");
        }

        boolean updated = userService.updatePasswordByEmail(email, newPassword);
        if (updated) {
        	session.removeAttribute("forgotOtp");
            session.removeAttribute("forgotEmail");
            session.removeAttribute("forgotOtpExpiry");
            session.removeAttribute("forgotOtpVerified");
            return ResponseEntity.ok("Password updated successful.");
        } else {
            return ResponseEntity.badRequest().body("Error updating password.");
        }
    }

}

