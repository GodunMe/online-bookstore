package fa.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fa.training.model.Book;
import fa.training.model.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findAllByOrderByBookIdDesc();
	Page<Book> findAll(Pageable pageable);
	Optional<Book> findByISBN(String isbn);
	Page<Book> findByCategory_CategoryName(String categoryName, Pageable pageable);
	Optional<Book> findByBookId(Long bookId);
	Page<Book> findByBookTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrISBNContainingIgnoreCase(String query, String query1, String query2, Pageable pageable);
	Optional<Book> findBookByBookId(Long id);
	List<Book> findAllByCategory(Category category);
			
}
