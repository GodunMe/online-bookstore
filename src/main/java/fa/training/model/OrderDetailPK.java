package fa.training.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderDetailPK implements Serializable {
	
	@Column(name = "order_id", updatable = false)
	private Long orderId;
	
	@Column(name = "book_id", updatable = false)
	private Long bookId;
	
	public OrderDetailPK() {}

	public OrderDetailPK(Long orderId, Long bookId) {
		super();
		this.orderId = orderId;
		this.bookId = bookId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getBookId() {
		return bookId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookId, orderId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetailPK other = (OrderDetailPK) obj;
		return Objects.equals(bookId, other.bookId) && Objects.equals(orderId, other.orderId);
	}
}
