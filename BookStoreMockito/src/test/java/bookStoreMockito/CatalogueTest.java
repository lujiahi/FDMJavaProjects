package bookStoreMockito;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bookStoreMockito.Book;
import bookStoreMockito.Catalogue;
import bookStoreMockito.ReadItemCommand;
import bookStoreMockito.WriteItemCommand;


@ExtendWith(MockitoExtension.class)
public class CatalogueTest {
	
	@Mock
	private ReadItemCommand reader;
	
	@Mock
	private WriteItemCommand writer;
	
	private Catalogue cat; 
	
	@BeforeEach
	void setup() {
		cat = new Catalogue(reader, writer);
	}
	
	@Test
	public void test_getAllBooks_ReturnsEmptyBookList_IfNoBooksAreInTheCatalogue() {
		when(reader.readAll()).thenReturn(null);
		Book[] bookList = cat.getAllBooks();
		assertEquals(bookList.length, 0);
	}
	
	@Test
	public void test_getAllBooks_CallsReadAllMethodOfReadItemCommand_WhenCalled() {
		cat.getAllBooks();
		verify(reader, times(1)).readAll();
	}
	
	@Test
	public void test_getAllBooks_ReturnsSameListAsFromReadAll() {
		Book java = new Book("java");
		Book[] bookList = new Book[1];
		bookList[0] = java;
		when(reader.readAll()).thenReturn(bookList);
		Book[] bookListCalled = cat.getAllBooks();
		assertArrayEquals(bookList, bookListCalled);	
	}
	
	@Test
	public void test_addBook_AddsSingleBook() {
		Book java = new Book("java");
		when(writer.insertItem(java)).thenReturn(true);
		assertTrue(cat.addBook(java));
	}

	@Test
	public void test_addBooks_AddsMultipleBooks() {
		Book java = new Book("java");
		Book python = new Book("python");
		Book[] bookList = new Book[2];
		bookList[0] = java;
		bookList[1] = python;
		cat.addBooks(bookList);
		verify(writer, times(1)).insertItem(java);
		verify(writer, times(1)).insertItem(python);
	}
	
	@Test
	public void test_getBook_ReturnsBookWithISBN() {
		Book java = new Book("12345","java");
		when(reader.getItem("12345")).thenReturn(java);
		assertEquals(cat.getBook("12345").getIsbn(), "12345");
	}
	
	@Test
	public void test_deleteBook() {
		Book java = new Book("java");
		cat.deleteBook(java);
		verify(writer, times(1)).deleteItem(java);
	}
	
	@Test
	public void test_deleteAll() {
		Book java = new Book("java");
		Book[] bookList = new Book[2];
		bookList[0] = java;
		bookList[1] = java;
		when(reader.readAll()).thenReturn(bookList);	
		cat.deleteAllBooks();
		verify(reader, times(1)).readAll();
		verify(writer, times(2)).deleteItem(java);
	}
	

}
