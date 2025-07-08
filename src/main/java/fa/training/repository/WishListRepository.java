package fa.training.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fa.training.model.Book;
import fa.training.model.User;
import fa.training.model.WishListItem;
import fa.training.model.WishListItemPK;

public interface WishListRepository extends JpaRepository<WishListItem, WishListItemPK> {
    List<WishListItem> findByUser(User user);
    @Query("SELECT w.book FROM WishListItem w WHERE w.user = :user")
    Page<Book> findByUser(User user, Pageable pageable);
    boolean existsByUserAndBook(User user, Book book);
}

