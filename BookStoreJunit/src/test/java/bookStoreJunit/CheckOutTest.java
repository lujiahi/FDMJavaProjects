package bookStoreJunit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CheckOutTest {

	@Test
	@DisplayName("Calculate price returns zero when passed an empty basket")
	void test_CalculatePrice_ReturnsZero() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 0.0);
	}
	
	@Test
	@DisplayName("Calculate price returns the correct price of one book")
	void test_CalculatePrice_ReturnsPriceOfBookInBasket() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book = new Book();
		book.setPrice(1.0);
		basket.addBook(book);
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 1.0);		
	}
	
	@Test
	@DisplayName("Calculate price return the total price of two books")
	void test_CalculatePrice_ReturnsTotalPriceOfTwoBooks() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book1 = new Book(12345);
		Book book2 = new Book(54321);
		book1.setPrice(1.0);
		book2.setPrice(2.0);
		basket.addBook(book1);
		basket.addBook(book2);
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 2.85);	
	}
	
	@Test
	@DisplayName("Calculate price return the total price of three books with 1% Discount")
	void test_CalculatePrice_ReturnsTotalPriceOfThreeBooks() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book1 = new Book(12345);
		Book book2 = new Book(23456);
		Book book3 = new Book(34567);
		book1.setPrice(25.99);
		book2.setPrice(25.99);
		book3.setPrice(25.99);
		basket.addBook(book1);
		basket.addBook(book2);
		basket.addBook(book3);
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 73.33);	
	}
	
	@Test
	@DisplayName("Calculate price return the total price of seven books with 2% Discount")
	void test_CalculatePrice_ReturnsTotalPriceOfSevenBooks() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book1 = new Book(11111);
		book1.setPrice(25.99);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 165.56);	
	}
	
	@Test
	@DisplayName("Calculate price return the total price of seven books with 3% Discount + 10% Additional Discount")
	void test_CalculatePrice_ReturnsTotalPriceOfTenBooks() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book1 = new Book();
		book1.setPrice(25.99);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book1);
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 215.55);	
	}
	
	@Test
	@DisplayName("Calculate price return the total price of three books with 1% Discount + 5% Distinct Discount")
	void test_CalculatePrice_ReturnsTotalPriceOfSevenDifferentBooks() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book1 = new Book(12345);
		Book book2 = new Book(23456);
		Book book3 = new Book(34567);
		book1.setPrice(25.99);
		book2.setPrice(10.5);
		book3.setPrice(7.7);
		basket.addBook(book3);
		basket.addBook(book2);
		basket.addBook(book1);
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 41.56);	
	}
	
	@Test
	@DisplayName("Calculate price return the total price of three books with 50% Discount on bundle and additional 1%")
	void test_CalculatePrice_ReturnsTotalPriceOfThreeBooksWithTwoBeingTheSame() {
		Basket basket = new Basket();
		CheckOut co = new CheckOut();
		Book book1 = new Book(12345);
		Book book2 = new Book(34567);
		book1.setPrice(25.99);
		book2.setPrice(7.7);
		basket.addBook(book1);
		basket.addBook(book1);
		basket.addBook(book2);
		double totalPrice = co.calculatePrice(basket);
		assertEquals(totalPrice, 46.22);	
	}

}
