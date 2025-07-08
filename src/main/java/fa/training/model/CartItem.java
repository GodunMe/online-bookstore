package fa.training.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "CartItems")
public class CartItem implements Serializable{

	@EmbeddedId
	private CartItemPK cartItemId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@ManyToOne
	@MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "FK_CartItem_User"), updatable = false)
    private User user;
	
	@ManyToOne
	@MapsId("bookId")
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", foreignKey = @ForeignKey(name = "FK_CartItem_Book"), updatable = false)
    private Book book;

	public CartItem() {}

	public CartItem(Integer quantity, User user, Book book) {
		this.quantity = quantity;
		this.user = user;
		this.book = book;
		this.cartItemId = new CartItemPK(user.getUserId(), book.getBookId());
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public User getUser() {
		return user;
	}

	public Book getBook() {
		return book;
	}

	public CartItemPK getCartItemId() {
		return cartItemId;
	}
}
