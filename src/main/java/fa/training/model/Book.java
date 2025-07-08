package fa.training.model;

import java.io.Serializable;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "Books")
public class Book implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", updatable = false)
    private Long bookId;

    @Column(name = "book_title", columnDefinition = "NVARCHAR(100)")
    private String bookTitle;

    @Column(name = "author", columnDefinition = "NVARCHAR(50)")
    private String author;
    
    @Column(name = "book_url", columnDefinition = "NVARCHAR(255)")
    private String bookURL;
    
    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;
    
    @Column(name = "ISBN", columnDefinition = "NVARCHAR(50)", unique = true)
    private String ISBN;
    
    @Column(name = "stock_quantity")
    private int stockQuantity;
    
    @Column(name = "price")
    private int price;
    
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<CartItem> cartItems;
    
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", foreignKey = @ForeignKey(name = "FK_Book_Category"))
    private Category category;
    
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<OrderDetail> orderDetails;
    
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<WishListItem> wishListItem;
    
    public Book() {}

	public Book(String bookTitle, String author, String bookURL, String overview, String iSBN, int price, Category category) {
		this.bookTitle = bookTitle;
		this.author = author;
		this.bookURL = bookURL;
		this.overview = overview;
		ISBN = iSBN;
		this.price = price;
		this.category = category;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookURL() {
		return bookURL;
	}

	public void setBookURL(String bookURL) {
		this.bookURL = bookURL;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getBookId() {
		return bookId;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public List<WishListItem> getWishListItem() {
		return wishListItem;
	}

	public void setWishListItem(List<WishListItem> wishListItem) {
		this.wishListItem = wishListItem;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
    
    
}
