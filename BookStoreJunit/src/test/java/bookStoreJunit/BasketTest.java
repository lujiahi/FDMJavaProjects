package bookStoreJunit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasketTest {

	@Test
	@DisplayName("Basket returns an empty list if no books have been added")
	void test_GetBooksInBasket_ReturnsEmptyBookList() {
		Basket basket = new Basket();
		Book[] bookList = basket.getBooksInBasket();
		assertEquals(bookList.length, 0);
	}
	
	@Test
	@DisplayName("Basket returns array of length one after one book has been added")
	void test_GetBooksInBasket_ReturnsArrayOfLengthOne() {
		Basket basket = new Basket();
		Book book = new Book();
		basket.addBook(book);
		Book[] bookList = basket.getBooksInBasket();
		assertEquals(bookList.length, 1);	
	}
	
	@Test
	@DisplayName("Basket returns array of length two after two books have been added")
	void test_GetBooksInBasket_ReturnsArrayOfLengthTwo() {
		Basket basket = new Basket();
		Book book1 = new Book();
		Book book2 = new Book();
		basket.addBook(book1);
		basket.addBook(book2);
		Book[] bookList = basket.getBooksInBasket();
		assertEquals(bookList.length, 2);
	}

}
