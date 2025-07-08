	package fa.training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import fa.training.model.Book;
import fa.training.model.Category;
import fa.training.model.User;
import fa.training.service.BookService;
import fa.training.service.CategoryService;
import fa.training.service.WishListService;
import fa.training.util.FileHandle;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Controller
public class HomeController {

    private final CategoryService categoryService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private WishListService wishListService;

    HomeController(CategoryService categoryService) {
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
	
	@GetMapping("/home")
	public String home(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
		Page<Book> booksPage = bookService.getBookByPage(PageRequest.of(page, size));
	    model.addAttribute("booksPage", booksPage);
	    model.addAttribute("currentPage", page);
	    
	    return "home";
	}
	
	@GetMapping("/search")
	public String searchBooks(@RequestParam("query") String query, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size, Model model) {
	    Page<Book> bookPage = bookService.getBooksByQuery(query, page, size);
	    model.addAttribute("booksPage", bookPage);
	    model.addAttribute("books", bookPage.getContent());
	    model.addAttribute("searchQuery", query);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", bookPage.getTotalPages());

	    return "search-results";
	}
	
	@GetMapping("/category/{categoryName}")
	public String booksByCategory(@PathVariable String categoryName, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size, Model model) {
	    Page<Book> bookPage = bookService.getBooksByCategory(categoryName, page, size);
	    model.addAttribute("books", bookPage.getContent());
	    model.addAttribute("selectedCategory", categoryName);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", bookPage.getTotalPages());

	    return "category-books";
	}

	@PostMapping("/home")
    public String submitForm() {
        return "home";
    }
	
	@GetMapping("/{isbn}")
	public String bookDetail(@PathVariable("isbn") String isbn, Model model, HttpSession session) {
		Book book = bookService.getBookWithISBN(isbn);
		User user = (User) session.getAttribute("loggedInUser");
		boolean isInWishList = false;
	    if (book != null) {
	        model.addAttribute("book", book);
	        String overviewHtml =  FileHandle.readOverview(book.getOverview());
            model.addAttribute("overviewFromFile", overviewHtml);
            if (user != null) {
            	isInWishList = wishListService.isBookInWishList(user, book);
            	model.addAttribute("isInWishList", isInWishList);
            }
	        return "book-detail";
	    } else {
	        return "redirect:/home";
	    }
	}
	
	@GetMapping("/aboutUs")
	public String aboutUs() {
		return "aboutUs";
	}
	
	@GetMapping("/help")
	public String help() {
		return "help";
	}
	
	@GetMapping("/order-guide")
	public String orderGuide() {
		return "order-guide";
	}
	
	@GetMapping("/payment-guide")
	public String paymentGuide() {
		return "payment-guide";
	}
	
	@GetMapping("/shipping-methods")
	public String shipingMethod() {
		return "shipping-methods";
	}
	
	@GetMapping("/logout")
    public String logout(HttpSession session) {
        String email = (String) session.getAttribute("userLogin");
        session.invalidate();
		if (email == null || !email.equals("admin@gmail.com")) {
			return "redirect:/home";
		}
		return "redirect:/login";
    }
	
	@GetMapping("/forgot-password")
	public String forgotPasswordPage(HttpSession session) {
	    User loggedInUser = (User) session.getAttribute("loggedInUser");
	    if (loggedInUser != null) {
	        return "redirect:/home"; 
	    }
	    return "forgot-password";
	}
}
