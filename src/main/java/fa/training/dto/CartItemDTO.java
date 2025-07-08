package fa.training.dto;

public class CartItemDTO {
	private Long bookId;
	private String author;
	private String bookTitle;
	private String bookURL;
	private int quantity;
	private int price;
	
	
	public CartItemDTO() {
	}

	public CartItemDTO(Long bookId, String author, String bookTitle, String bookURL, int quantity, int price) {
		super();
		this.bookId = bookId;
		this.author = author;
		this.bookTitle = bookTitle;
		this.bookURL = bookURL;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getBookURL() {
		return bookURL;
	}

	public void setBookURL(String bookURL) {
		this.bookURL = bookURL;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
