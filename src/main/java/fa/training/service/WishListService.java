package fa.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fa.training.model.Book;
import fa.training.model.User;
import fa.training.model.WishListItem;
import fa.training.model.WishListItemPK;
import fa.training.repository.WishListRepository;

@Service
public class WishListService {

	@Autowired
    private WishListRepository wishListRepository;


    public void addBookToWishList(User user, Book book) {
    	WishListItem item = new WishListItem();
    	item.setWishListId(new WishListItemPK(user.getUserId(),book.getBookId()));
        item.setUser(user);
        item.setBook(book);
        wishListRepository.save(item);
    }
    
    public List<WishListItem> getUserWishList(User user){
    	return wishListRepository.findByUser(user);
    }
    public Page<Book> getWishListBooks(User user, Pageable pageable) {
        return wishListRepository.findByUser(user, pageable);
    }

	public void removeBookFromWishList(User user, Book book) {
		wishListRepository.deleteById(new WishListItemPK(user.getUserId(),book.getBookId()));;		
	}
	public boolean isBookInWishList(User user, Book book) {
	    return wishListRepository.existsByUserAndBook(user, book);
	}


}
