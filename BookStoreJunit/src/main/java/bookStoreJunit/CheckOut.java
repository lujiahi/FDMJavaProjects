package bookStoreJunit;

import java.util.ArrayList;

public class CheckOut {
	
	public double calculatePrice(Basket basket) {
		Book[] bookList = basket.getBooksInBasket();
		double totalPrice = 0.0;
		ArrayList<Integer> distinctBooks = new ArrayList<Integer>();
		ArrayList<Integer> distinctDuplicates = new ArrayList<Integer>();
		
		// If the basket contains 2 of the same book, apply a unique discount of 50% to those two books only.
		// Any subsequent books that are the same will not get the discount. 
		// If the basket has multiple sets of two that are the same, a 50% discount is applied to each set.
		for(Book book : bookList) {
			totalPrice += book.getPrice();
			if(!distinctBooks.contains(book.getIsbn())) {
				distinctBooks.add(book.getIsbn());
			}
			else {
				if(!distinctDuplicates.contains(book.getIsbn())) {
					distinctDuplicates.add(book.getIsbn());
					totalPrice -= book.getPrice() * 0.5;
				}
			}
		}
		
		// For every 3 books in the basket, the user gains an accumulative 1% discount
		int numOfCumulativeDiscount = bookList.length / 3;
		totalPrice = totalPrice - 0.01 * numOfCumulativeDiscount * totalPrice;
		
		// Ten or more books grants a 10% discount for the whole basket in addition to the above discount. 
		// This is a one-time discount and does not apply multiple times.
		if(bookList.length >= 10) {
			totalPrice *= 0.9;
		}
		
		// If every book in the Basket is different, apply an additional 5% to the whole basket
		if(distinctBooks.size() == bookList.length && distinctBooks.size() > 1) {
			totalPrice *= 0.95;
		}
		
		totalPrice = Math.round(totalPrice * 100.0) / 100.0;

		return totalPrice;
	}

}
