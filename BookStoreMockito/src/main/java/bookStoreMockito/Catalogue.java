package bookStoreMockito;

public class Catalogue {
	private ReadItemCommand reader;
	private WriteItemCommand writer;

	public Book[] getAllBooks() {
		Book[] books = reader.readAll();
		if(books == null) {
			return new Book[0];
		}
		else {
			return books;
		}

	}

	public Catalogue(ReadItemCommand reader,WriteItemCommand writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public boolean addBook(Book book) {
		return writer.insertItem(book);
	}
	
	public void addBooks(Book[] bookList) {
		for(Book book:bookList) {
			writer.insertItem(book);
		}
	}
	
	public Book getBook(String isbn) {
		Book book = reader.getItem(isbn);
		return book;
	}
	
	public boolean deleteBook(Book book) {
		return writer.deleteItem(book);
	}
	
	public void deleteAllBooks() {
		Book[] allBooks = reader.readAll();
		for(Book book: allBooks) {
			writer.deleteItem(book);
		}
	}

}
