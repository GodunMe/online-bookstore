package fa.training.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
//@Check(constraints = "gender in ('male', 'female', 'other')")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false)
	private Long userId;

	@Column(name = "first_name", columnDefinition = "NVARCHAR(50)")
	private String firstName;

	@Column(name = "last_name", columnDefinition = "NVARCHAR(50)")
	private String lastName;

	@Column(name = "gender", columnDefinition = "NVARCHAR(10)")
	private String gender;

	@Column(name = "phone", columnDefinition = "VARCHAR(13)", unique = true)
	private String phone;

	@Column(name = "email", columnDefinition = "VARCHAR(50)", unique = true)
	private String email;

	@Column(name = "password", columnDefinition = "VARCHAR(64)")
	private String password;

	@Column(name = "role", columnDefinition = "VARCHAR(15)")
	private String role;
	
	@Column(name = "fail_attempts", columnDefinition = "TINYINT")
	private Byte failAttempt;
	
	@Column(name = "lock_time")
	private LocalDateTime lockTime;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<CartItem> cartItems;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Order> orders;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<WishListItem> wishListItem;

	public User() {
		super();
	}

	public User(Long userId, String firstName, String lastName, String gender, String phone, String email,
			String password, String role) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public Byte getFailAttempt() {
		return failAttempt;
	}

	public void setFailAttempt(Byte failAttempt) {
		this.failAttempt = failAttempt;
	}
	
	public LocalDateTime getLockTime() {
	    return lockTime;
	}

	public void setLockTime(LocalDateTime lockTime) {
	    this.lockTime = lockTime;
	}

	public List<WishListItem> getWishListItem() {
		return wishListItem;
	}
	
	

	

}
