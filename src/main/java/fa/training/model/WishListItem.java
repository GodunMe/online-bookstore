package fa.training.model;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "WishList")
public class WishListItem implements Serializable{
	@EmbeddedId
	private WishListItemPK wishListId;
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "FK_WishListItem_User"), updatable = false)
	private User user;

	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name = "book_id", referencedColumnName = "book_id", foreignKey = @ForeignKey(name = "FK_WishListItem_Book"), updatable = false)
	private Book book;

	public WishListItem() {
	}

	public WishListItemPK getWishListId() {
		return wishListId;
	}

	public void setWishListId(WishListItemPK wishListId) {
		this.wishListId = wishListId;
	}

	public WishListItem(User user, Book book) {
		this.user = user;
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
