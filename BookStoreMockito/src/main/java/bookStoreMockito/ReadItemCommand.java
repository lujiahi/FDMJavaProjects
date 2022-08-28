package bookStoreMockito;

public interface ReadItemCommand {
	
	Book[] readAll();
	Book getItem(String isbn);

}
