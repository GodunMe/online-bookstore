package fa.training.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fa.training.model.Book;
import fa.training.service.BookService;
import fa.training.service.CategoryService;
import fa.training.util.FileHandle;

@RestController
public class BookRestControlller {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/data/books")
	@ResponseBody
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> lb = bookService.getAllBooks();
		System.out.println(lb.size());
		if (lb.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(lb);
		}
	}
	
	@GetMapping("/data/books/{isbn}")
	@ResponseBody
	public ResponseEntity<Book> getBookWithISBN(@PathVariable String isbn) {
		Book book = bookService.getBookWithISBN(isbn);
		if (book == null) {
			return ResponseEntity.notFound().build();
		} else {
			book.setOverview(FileHandle.readOverview(book.getOverview()));
			System.out.println(book.getOverview());
			return ResponseEntity.ok(book);
		}
	}
	
	@PostMapping("/data/books/add")
	@ResponseBody
	public ResponseEntity<String> addNewBook(
			@RequestParam("bookTitle") String bookTitle,
			@RequestParam("author") String author,
			@RequestParam("category") String category,
			@RequestParam("isbn") String isbn,
			@RequestParam("price") String price,
			@RequestParam("overview") String overview,
	        @RequestParam("image") MultipartFile image) {

		
		Book book = bookService.getBookWithISBN(isbn);
		if (book != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ISBN is already in use.");
		}
		
		String imageName = FileHandle.saveImage(image, isbn);
		if (imageName == null)  {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
		}
		
		String overviewName = FileHandle.saveOverview(overview, isbn);
		if (overviewName == null)  {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
		}
		Book newBook = new Book();
		newBook.setBookTitle(bookTitle);
		newBook.setAuthor(author);
		newBook.setCategory(categoryService.getCategoryWithName(category));
		newBook.setISBN(isbn);
		newBook.setPrice(Integer.parseInt(price));
		newBook.setOverview(overviewName);
		newBook.setStockQuantity(0);
		newBook.setBookURL(imageName);
		bookService.save(newBook);
		return ResponseEntity.ok("Added.");
	}
	
	@PutMapping("/data/book-detail/add-quantity")
	@ResponseBody
	public ResponseEntity<String> addBookQuantity(
			@RequestParam("isbn") String isbn,
			@RequestParam("quantity-add") String quantity) {
		int quantityAdd = Integer.parseInt(quantity);
		System.out.println(quantityAdd);
		System.out.println(isbn);
		Book book = bookService.getBookWithISBN(isbn);
		if (book == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Update failed");
		}
		book.setStockQuantity(book.getStockQuantity() + Integer.parseInt(quantity) > 0 ? book.getStockQuantity() + Integer.parseInt(quantity) : 0);
		bookService.save(book);
		return ResponseEntity.ok("Added.");
	}
	
	@PutMapping("/data/book-detail/update")
	@ResponseBody
	public ResponseEntity<String> updateNewBook(
			@RequestParam("bookTitle") String bookTitle,
			@RequestParam("author") String author,
			@RequestParam("category") String category,
			@RequestParam("isbn") String isbn,
			@RequestParam("price") String price,
			@RequestParam("overview") String overview) {

		System.out.println("ISBN:" + isbn);
		
		Book book = bookService.getBookWithISBN(isbn);
		if (book == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book does not exist, please reload the page.");
		}
		
		String oldOverview = FileHandle.readOverview(book.getOverview());
		if (!oldOverview.equals(overview) || !book.getBookTitle().equals(bookTitle)) {
			System.out.println("Change cover");
			FileHandle.deleteFile(book.getOverview(), "overview");
			String overviewName = FileHandle.saveOverview(overview, isbn);
			if (overviewName == null)  {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
			}
			book.setOverview(overviewName);
		}
		
		book.setBookTitle(bookTitle);
		book.setAuthor(author);
		book.setCategory(categoryService.getCategoryWithName(category));
		book.setPrice(Integer.parseInt(price));
		
		bookService.save(book);
		return ResponseEntity.ok("Updated.");
	}
	
	@PutMapping("/data/book-detail/update-with-cover")
	@ResponseBody
	public ResponseEntity<String> updateNewBookWithNewCover(
			@RequestParam("bookTitle") String bookTitle,
			@RequestParam("author") String author,
			@RequestParam("category") String category,
			@RequestParam("isbn") String isbn,
			@RequestParam("price") String price,
			@RequestParam("overview") String overview,
	        @RequestParam("image") MultipartFile image) {

		System.out.println("ISBN:" + isbn);
		
		Book book = bookService.getBookWithISBN(isbn);
		if (book == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book does not exist, please reload the page.");
		}
		
		if (image != null ) {
			System.out.println("Change cover");
			FileHandle.deleteFile(book.getBookURL(), "cover");
			String imageName = FileHandle.saveImage(image, isbn);
			if (imageName == null)  {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
			}
			book.setBookURL(imageName);
		}
		
		String oldOverview = FileHandle.readOverview(book.getOverview());
		if (!oldOverview.equals(overview) || !book.getBookTitle().equals(bookTitle)) {
			System.out.println("Change cover");
			FileHandle.deleteFile(book.getOverview(), "overview");
			String overviewName = FileHandle.saveOverview(overview, isbn);
			if (overviewName == null)  {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
			}
			book.setOverview(overviewName);
		}
		
		book.setBookTitle(bookTitle);
		book.setAuthor(author);
		book.setCategory(categoryService.getCategoryWithName(category));
		book.setPrice(Integer.parseInt(price));
		
		bookService.save(book);
		return ResponseEntity.ok("Updated.");
	}
	
	@DeleteMapping("/data/book-detail/delete/{isbn}")
	@ResponseBody
	public ResponseEntity<String> deleteBook(@PathVariable String isbn) {

		System.out.println("ISBN:" + isbn);
		
		Book book = bookService.getBookWithISBN(isbn);
		if (book == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book does not exist, please reload the page.");
		}
		
		FileHandle.deleteFile(book.getBookURL(), "cover");
		FileHandle.deleteFile(book.getOverview(), "overview");
		
		bookService.delete(book);
		return ResponseEntity.ok("Deleted.");
	}
	
}
