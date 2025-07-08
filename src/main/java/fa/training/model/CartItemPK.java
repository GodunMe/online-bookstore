package fa.training.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CartItemPK implements Serializable{

	@Column(name = "user_id", updatable = false)
	private Long userId;
	
	@Column(name = "book_id", updatable = false)
	private Long bookId;
	
	public CartItemPK() {}

	public CartItemPK(Long userId, Long bookId) {
		this.userId = userId;
		this.bookId = bookId;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getBookId() {
		return bookId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItemPK other = (CartItemPK) obj;
		return Objects.equals(bookId, other.bookId) && Objects.equals(userId, other.userId);
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	
}
