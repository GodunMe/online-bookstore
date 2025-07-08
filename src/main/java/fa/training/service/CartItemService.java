package fa.training.service;

import java.util.List;
import java.util.Optional;
import fa.training.repository.CategoryRepository;
import fa.training.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fa.training.controller.RegisterController;
import fa.training.model.Book;
import fa.training.model.CartItem;
import fa.training.model.CartItemPK;
import fa.training.model.User;
import fa.training.repository.BookRepository;
import fa.training.repository.CartItemRepository;

@Service
public class CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;
	
	public List<CartItem> findByUser(User user){
		return cartItemRepository.findByUser(user);
	}
}
