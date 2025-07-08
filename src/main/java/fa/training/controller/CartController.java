package fa.training.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import fa.training.dto.CartItemDTO;
import fa.training.model.CartItem;
import fa.training.model.Category;
import fa.training.model.User;
import fa.training.service.CartItemService;
import fa.training.service.CategoryService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
	CartItemService cartItemService;
	
	private final CategoryService categoryService;

	CartController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
	
	@ModelAttribute
    public void addLoggedInUserToModel(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        }
    }
	
	@ModelAttribute("categories")
	public List<Category> getAllCategories() {
	    return categoryService.getAllCategories();
	}
	
	@GetMapping("/session/cart")
	public String viewCart(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("loggedInUser");
	    if (user == null) {
	        return "redirect:/login";
	    }

	    Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
	    if (cart == null || cart.isEmpty()) {
	        model.addAttribute("cartItems", new ArrayList<CartItemDTO>());
	        model.addAttribute("totalPrice", 0);
	        return "cart";
	    }

	    List<CartItemDTO> cartItems = new ArrayList<>(cart.values());

	    int totalPrice = cartItems.stream()
	        .mapToInt(item -> item.getQuantity() * item.getPrice())
	        .sum();

	    model.addAttribute("cartItems", cartItems);
	    model.addAttribute("totalPrice", totalPrice);

	    return "cart";
	}

}
