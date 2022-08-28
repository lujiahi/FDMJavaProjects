package bookStoreJunit;


public class Book {
	private double price;
	private int isbn;
	
	public Book() {
		
	}
	
	public Book(int isbn) {
		this.isbn = isbn;
	}
	
	public Book(int isbn, double price) {
		this.isbn = isbn;
		this.price = price;
	}
	
	public int getIsbn() {
		return this.isbn;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
}
