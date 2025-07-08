package fa.training.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fa.training.model.Book;
import fa.training.model.CartItem;
import fa.training.model.CartItemPK;
import fa.training.model.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemPK> {
	List<CartItem> findByUser(User user);
}
