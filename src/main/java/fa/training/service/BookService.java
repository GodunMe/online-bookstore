package fa.training.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import fa.training.model.Book;
import fa.training.model.Category;
import fa.training.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAllByOrderByBookIdDesc();
	}

	public Page<Book> getBookByPage(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}

	public Page<Book> getBooksByCategory(String categoryName, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return bookRepository.findByCategory_CategoryName(categoryName, pageable);
	}

	public Page<Book> getBooksByQuery(String query, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> result = bookRepository
				.findByBookTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrISBNContainingIgnoreCase(query, query,
						query, pageable);
		return result;
	}

	public Book getBookWithISBN(String isbn) {
		Optional<Book> book = bookRepository.findByISBN(isbn);
		return book.isPresent() ? book.get() : null;
	}

	public int getNumberOfBookOfCategory(Category category) {
		List<Book> bookList = bookRepository.findAllByCategory(category);
		return bookList == null ? 0 : bookList.size();
	}

	public void save(Book book) {
		bookRepository.save(book);
	}

	public Book findBookByBookId(Long id) {
		Optional<Book> book = bookRepository.findBookByBookId(id);
		return book.isPresent() ? book.get() : null;
	}

	public Book findById(Long bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		return book.isPresent() ? book.get() : null;
	}

	public void delete(Book book) {
		bookRepository.delete(book);
	}

}
