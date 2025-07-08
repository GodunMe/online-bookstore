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
@Table(name = "OrderDetails")
public class OrderDetail implements Serializable {

	@EmbeddedId
	private OrderDetailPK orderDetailId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "price")
	private Double price;
	
	@ManyToOne
	@MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", foreignKey = @ForeignKey(name = "FK_OrderDetail_Order"), updatable = false)
    private Order order;
	
	@ManyToOne
	@MapsId("bookId")
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", foreignKey = @ForeignKey(name = "FK_OrderDetail_Book"), updatable = false)
    private Book book;
	
	public OrderDetail() {}

	public OrderDetail(Integer quantity, Double price, Order order, Book book) {
		this.quantity = quantity;
		this.price = price;
		this.order = order;
		this.book = book;
		this.orderDetailId = new OrderDetailPK(order.getOrderId(), book.getBookId());
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Order getOrder() {
		return order;
	}

	public Book getBook() {
		return book;
	}

	public OrderDetailPK getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(OrderDetailPK orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
}
