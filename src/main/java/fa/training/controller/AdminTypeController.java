package fa.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminTypeController {

	@GetMapping("/admin/{type}")
	public String dashboard(HttpSession session, @PathVariable String type) {
		String email = (String) session.getAttribute("userLogin");
		if (email == null || !email.equals("admin@gmail.com")) {
			return "error/404";
		}
		return "admin/" + type;
	}
	
}
