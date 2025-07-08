package fa.training.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fa.training.model.User;
import fa.training.service.UserService;
import fa.training.util.Encryption;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@Autowired
	private Environment environment;
	
	@Autowired
    private UserService userService;
	
	private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_TIME_IN_MINUTES = 5;
    
    

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model, HttpSession session) {
    	if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/home";
        }
    	if (session.getAttribute("userLogin") != null && ((String)session.getAttribute("userLogin")).equals(environment.getProperty("system.default.username"))) {
            return "redirect:/admin";
        }
    	Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");
        LocalDateTime lockTime = (LocalDateTime) session.getAttribute("lockTime");

        if (failedAttempts == null) failedAttempts = 0;
        boolean isLocked = lockTime != null && LocalDateTime.now().isBefore(lockTime.plusMinutes(LOCK_TIME_IN_MINUTES));

        if (isLocked) {
            model.addAttribute("errorMessage", "Too many failed attempts. Try again later!");
            model.addAttribute("isLocked", true);
        } else {
            model.addAttribute("isLocked", false);
        }

        return "login";
    }


    @PostMapping("/login")
    public String doLogin(@RequestParam("email") String email, @RequestParam("password") String password,
                          HttpSession session, RedirectAttributes redirectAttributes) {
    	String defaultUsername = environment.getProperty("system.default.username");
        String defaultPassword = environment.getProperty("system.default.password");
        
        if (email.equals(defaultUsername) && password.equals(defaultPassword)) {
            session.setAttribute("userLogin", email);
            return "redirect:/admin";
        }
        
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if account is currently locked
            LocalDateTime lockTime = user.getLockTime();
            if (lockTime != null && LocalDateTime.now().isBefore(lockTime.plusMinutes(5))) {
                redirectAttributes.addFlashAttribute("errorMessage",
                    "Too many failed attempts. Please try again after 5 minutes.");
                return "redirect:/login";
            }
            
            if (user.getPassword().equals(Encryption.sha256(password))) {
                user.setFailAttempt((byte) 0);      
                user.setLockTime(null);           
                userService.save(user);

                session.setAttribute("loggedInUser", user);
                return "redirect:/home" ;
            } else {
                // Wrong password
                byte attempts = user.getFailAttempt() != null ? user.getFailAttempt() : 0;
                attempts++;

                user.setFailAttempt(attempts);

                if (attempts >= MAX_FAILED_ATTEMPTS) {
                    user.setLockTime(LocalDateTime.now()); // Start lock period
                    redirectAttributes.addFlashAttribute("errorMessage",
                        "Too many failed attempts. Your account is locked for 5 minutes.");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage",
                        "Invalid email or password. Attempts left: " + (MAX_FAILED_ATTEMPTS - attempts));
                }

                userService.save(user);
            }

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password.");
        }

        return "redirect:/login";
    }



}
