package fa.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminHomeController {

	@GetMapping("/admin")
	public String adminHomePage(HttpSession session) {
		String email = (String) session.getAttribute("userLogin");
		if (email == null || !email.equals("admin@gmail.com")) {
			return "error/404";
		}
		
		return "admin/admin";
	}
	
}
