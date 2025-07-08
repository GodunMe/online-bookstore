package fa.training.controller;

import fa.training.model.User;
import fa.training.service.UserService;
import fa.training.util.Encryption;
import fa.training.util.FormatValidation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/edit-profile")
    public String showEditProfileForm(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "editProfile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(@RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("gender") String gender,
                                @RequestParam("phone") String phone,
                                @RequestParam(value = "oldPassword", required = false) String oldPassword,
                                @RequestParam(value = "newPassword", required = false) String newPassword,
                                @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                                HttpSession session,
                                Model model) {

        User sessionUser = (User) session.getAttribute("loggedInUser");

        if (sessionUser == null) {
            return "redirect:/login";
        }

        boolean hasError = false;

        // Validate name and phone
        if (!FormatValidation.isValidNameFormat(firstName)) {
            model.addAttribute("firstNameError", "Only letters and spaces allowed.");
            hasError = true;
        }

        if (!FormatValidation.isValidNameFormat(lastName)) {
            model.addAttribute("lastNameError", "Only letters and spaces allowed.");
            hasError = true;
        }

        if (!FormatValidation.isValidPhoneFormat(phone)) {
            model.addAttribute("phoneError", "Phone must be exactly 10 digits.");
            hasError = true;
        }

        // Validate password fields
        boolean passwordFieldsFilled = oldPassword != null && !oldPassword.isBlank()
                                     || newPassword != null && !newPassword.isBlank()
                                     || confirmPassword != null && !confirmPassword.isBlank();

        if (passwordFieldsFilled) {
            if (oldPassword == null || !Encryption.sha256(oldPassword).equals(sessionUser.getPassword())) {
                model.addAttribute("oldPasswordError", "Old password is incorrect.");
                hasError = true;
            }

            if (!FormatValidation.isValidPasswordFormat(newPassword)) {
                model.addAttribute("newPasswordError", "Password must be 8–16 characters.");
                hasError = true;
            }
            else
            	if(oldPassword.equals(newPassword)) {
            		model.addAttribute("newPasswordError", "New password must be different.");
            		hasError = true;
            	}

            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("confirmPasswordError", "Passwords do not match.");
                hasError = true;
            }
        }

        model.addAttribute("user", sessionUser);

        if (hasError) {
            return "editProfile";
        }

        // Check changing or not
        boolean isUpdated = false;

        if (!firstName.equals(sessionUser.getFirstName())) {
            sessionUser.setFirstName(firstName);
            isUpdated = true;
        }

        if (!lastName.equals(sessionUser.getLastName())) {
            sessionUser.setLastName(lastName);
            isUpdated = true;
        }
        
        if (!gender.equals(sessionUser.getGender())) {
            sessionUser.setGender(gender);
            isUpdated = true;
        }

        if (!phone.equals(sessionUser.getPhone())) {
            sessionUser.setPhone(phone);
            isUpdated = true;
        }

        if (passwordFieldsFilled && newPassword.equals(confirmPassword) && !newPassword.equals(sessionUser.getPassword())) {
            sessionUser.setPassword(newPassword);
            isUpdated = true;
        }

        if (isUpdated) {
            userService.update(sessionUser);
            model.addAttribute("success", "Profile updated successfully!");
        }

        return "editProfile";
    }

}
