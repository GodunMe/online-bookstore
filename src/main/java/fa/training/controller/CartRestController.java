package fa.training.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fa.training.dto.CartItemDTO;
import fa.training.model.Book;
import fa.training.model.Order;
import fa.training.model.OrderDetail;
import fa.training.model.OrderDetailPK;
import fa.training.model.User;
import fa.training.service.BookService;
import fa.training.service.OrderDetailService;
import fa.training.service.OrderService;
import jakarta.servlet.http.HttpSession;

import java.io.Console;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session/cart")
public class CartRestController {

	private final BookService bookService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public CartRestController(BookService bookService,
                              OrderService orderService,
                              OrderDetailService orderDetailService) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(HttpSession session,
                                            @RequestParam("bookId") Long bookId,
                                            @RequestParam("quantity") Integer quantity) {
        if (session.getAttribute("loggedInUser") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must login before adding to cart.");
        }

        Book book = bookService.findById(bookId);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }

        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();

        CartItemDTO item = cart.get(bookId);
        if (item == null) {
            cart.put(bookId, new CartItemDTO(bookId, book.getAuthor(), book.getBookTitle(), book.getBookURL(), quantity, book.getPrice()));
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        session.setAttribute("cart", cart);
        return ResponseEntity.ok("Book added to cart.");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCart(HttpSession session,
                                             @RequestParam("bookId") Long bookId,
                                             @RequestParam("quantity") Integer quantity) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null || !cart.containsKey(bookId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart.");
        }

        CartItemDTO item = cart.get(bookId);
        item.setQuantity(quantity);
        session.setAttribute("cart", cart);

        return ResponseEntity.ok("Cart updated successfully.");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(HttpSession session,
                                                 @RequestParam("bookId") Long bookId) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null || !cart.containsKey(bookId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart.");
        }

        cart.remove(bookId);
        session.setAttribute("cart", cart);
        return ResponseEntity.ok("Item removed from cart.");
    }
    
    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            session.setAttribute("error", "Your cart is empty.");
            return "redirect:/session/cart"; 
        }

        for (CartItemDTO item : cart.values()) {
            Book book = bookService.findById(item.getBookId());
            if (book == null || book.getStockQuantity() < item.getQuantity()) {
                session.setAttribute("error", "Insufficient stock for: " + item.getBookTitle());
                return "redirect:/session/cart"; 
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus("Pending");
        order.setTotalAmount((double) cart.values().stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum());
        orderService.save(order);

        for (CartItemDTO item : cart.values()) {
            Book book = bookService.findById(item.getBookId());

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setBook(book);
            detail.setOrderDetailId(new OrderDetailPK(order.getOrderId(), book.getBookId()));
            detail.setQuantity(item.getQuantity());
            detail.setPrice((double) item.getPrice());

            orderDetailService.save(detail);

            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
            bookService.save(book);
        }

        session.removeAttribute("cart");

        session.setAttribute("success", "Order placed successfully!");
        return "redirect:/orders/confirmation"; 
    }



}
