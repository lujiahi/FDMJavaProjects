package bookStoreJunit;


public class Basket {
	
	private Book[] bookList = new Book[0];

	public Book[] getBooksInBasket() {
		return this.bookList;
	}

	public void addBook(Book book) {
		int numBooks = bookList.length + 1;
		Book[] temp = bookList;
		bookList = new Book[numBooks];
		for(int i = 0; i < temp.length; i++) {
			bookList[i] = temp[i];
		}
		bookList[numBooks-1] = book;
	}

}
