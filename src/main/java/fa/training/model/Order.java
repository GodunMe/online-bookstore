package fa.training.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", updatable = false)
    private Long orderId;
	
    @Column(name = "order_date")
    private LocalDate orderDate;
    
    @Column(name = "total_amount")
    private Double totalAmount;
    
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
    
    @Column(name = "status", columnDefinition = "VARCHAR(15)")
    private String status;
    
    @Column(name = "address", columnDefinition = "VARCHAR(255)")
    private String address;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "FK_Order_User"))
    private User user;
    
    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<OrderDetail> orderDetails;
    
    @OneToOne(mappedBy = "order")
    private Payment payment;
    
    public Order() {}

	public Order(LocalDate orderDate, Double totalAmount, String status, String address, User user, Payment payment) {
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.status = status;
		this.address = address;
		this.user = user;
		this.payment = payment;
	}

	public Long getOrderId() {
		return orderId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
    
}
