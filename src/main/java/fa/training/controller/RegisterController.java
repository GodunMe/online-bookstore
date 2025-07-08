package fa.training.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import fa.training.form.RegisterForm;
import fa.training.model.User;
import fa.training.service.EmailService;
import fa.training.service.UserService;
import fa.training.util.FormatValidation;
import jakarta.servlet.http.HttpSession;

@Controller
public class RegisterController {

	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model, HttpSession session) {
		if (session.getAttribute("loggedInUser") != null) {
			return "redirect:/home";
		}

		if (model.containsAttribute("error")) {
			model.addAttribute("error", model.getAttribute("error"));
		}

		if (!model.containsAttribute("registerform")) {
			model.addAttribute("registerform", new RegisterForm());
		}
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute RegisterForm registerform, Model model, HttpSession session) {
	    String verifiedEmail = (String) session.getAttribute("otpVerifiedEmail");
	    Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");

	    // Check verify OTP
	    if (verifiedEmail == null || !verifiedEmail.equals(registerform.getEmail()) || otpVerified == null || !otpVerified) {
	        model.addAttribute("errors", Map.of("email", "Please verify your OTP before registering."));
	        model.addAttribute("registerform", registerform);
	        return "register";
	    }

	    session.removeAttribute("otpVerified");
	    session.removeAttribute("otpVerifiedEmail");

	    User user = registerform.toUser();
	    Map<String, String> result = userService.registerUser(user, registerform.getConfirmpassword());

	    if (result.containsKey("success")) {
	        return "redirect:/login";
	    } else {
	        registerform.setPassword("");
	        registerform.setConfirmpassword("");
	        model.addAttribute("errors", result);
	        model.addAttribute("registerform", registerform);
	        return "register";
	    }
	}


	@GetMapping("/send-otp")
	@ResponseBody
	public ResponseEntity<String> sendOtp(@RequestParam String email, HttpSession session) {
		System.out.println(email);
		if (!FormatValidation.isValidEmailFormat(email)) {
			return ResponseEntity.badRequest().body("Invalid Email");
		}

		String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
		session.setAttribute("otpCode", otp);
		session.setAttribute("otpExpiry", System.currentTimeMillis() + (5 * 60 * 1000));

		emailService.sendOtpEmail(email, otp);

		return ResponseEntity.ok("OTP code is sent");
	}

	@PostMapping("/verify-otp")
	@ResponseBody
	public ResponseEntity<String> verifyOtp(@RequestParam String otp, @RequestParam String email, HttpSession session) {
	    String sessionOtp = (String) session.getAttribute("otpCode");
	    Long expiry = (Long) session.getAttribute("otpExpiry");

	    if (sessionOtp == null || expiry == null || System.currentTimeMillis() > expiry) {
	        return ResponseEntity.badRequest().body("OTP has expired.");
	    }

	    if (!sessionOtp.equals(otp)) {
	        return ResponseEntity.badRequest().body("Invalid OTP.");
	    }

	    // Email is verified
	    session.setAttribute("otpVerifiedEmail", email);
	    session.setAttribute("otpVerified", true);

	    return ResponseEntity.ok("OTP is valid.");
	}

	public static void main(String[] args) {
		
	}

}
