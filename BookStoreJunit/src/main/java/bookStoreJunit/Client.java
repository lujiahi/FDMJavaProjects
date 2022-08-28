package bookStoreJunit;


public class Client {

	public static void main(String[] args) {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book1 = new Book(12345);
		Book book2 = new Book(23456);
		Book book3 = new Book(34567);
		book1.setPrice(25.99);
		book2.setPrice(21.99);
		book3.setPrice(20.99);
		basket.addBook(book1);
		basket.addBook(book2);
		basket.addBook(book3);
		double totalPrice = co.calculatePrice(basket);
		System.out.println("Total price: " + totalPrice);
	}
}
