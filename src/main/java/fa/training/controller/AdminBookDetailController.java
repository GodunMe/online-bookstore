package fa.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminBookDetailController {

	@GetMapping("/admin/book-detail/{isbn}")
	public String bookDetails(HttpSession session, @PathVariable String isbn, Model model) {
		String email = (String) session.getAttribute("userLogin");
		if (email == null || !email.equals("admin@gmail.com")) {
			return "error/404";
		}
		model.addAttribute("isbn", isbn);
		return "admin/book-detail";
	}
	
}
