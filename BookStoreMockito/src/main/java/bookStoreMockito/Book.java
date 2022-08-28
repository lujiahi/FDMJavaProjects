package bookStoreMockito;

public class Book {
	private String title;
	private String isbn;
	
	public Book(String title) {
		this.title = title;
	}
	
	public Book(String isbn, String title) {
		this.isbn = isbn;
		this.title = title;
	}
	
	public String getIsbn() {
		return isbn;
	}
}
