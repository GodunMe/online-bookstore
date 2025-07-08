package fa.training.controller;

import fa.training.model.Book;
import fa.training.model.User;
import fa.training.service.BookService;
import fa.training.service.WishListService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WishListController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private WishListService wishListService;
	
	@ModelAttribute
    public void addLoggedInUserToModel(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        }
    }
	
	@GetMapping("/wish-list")
	public String getWishList(Model model,
	                          HttpSession session,
	                          @RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "9") int size) {
	    User user = (User) session.getAttribute("loggedInUser");
	    if (user == null) {
	        return "redirect:/login";
	    }

	    Page<Book> wishListPage = wishListService.getWishListBooks(user, PageRequest.of(page, size));

	    model.addAttribute("wishListBooksPage", wishListPage.getContent());
	    model.addAttribute("wishListCurrentPage", page);
	    model.addAttribute("wishListTotalPages", wishListPage.getTotalPages());

	    return "wish-list";
	}



	@PostMapping("/add-to-wish-list")
	@ResponseBody
	public ResponseEntity<String> addToWishList(@RequestParam Long bookId, HttpSession session) {
	    User user = (User) session.getAttribute("loggedInUser");
	    
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
	    }

	    Book book = bookService.findBookByBookId(bookId);
	    if (book == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
	    }

	    try {
	        wishListService.addBookToWishList(user, book);
	        return ResponseEntity.ok("Book added to wish list");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add book to wish list");
	    }
	}
	@PostMapping("/remove-from-wish-list")
	@ResponseBody
	public ResponseEntity<String> removeFromWishList(@RequestParam Long bookId, HttpSession session) {
	    User user = (User) session.getAttribute("loggedInUser");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
	    }

	    Book book = bookService.findBookByBookId(bookId);
	    if (book == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
	    }

	    try {
	        wishListService.removeBookFromWishList(user, book);
	        return ResponseEntity.ok("Book removed from wish list");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove book from wish list");
	    }
	}



}
