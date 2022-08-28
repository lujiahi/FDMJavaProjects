package bookStoreMockito;

public interface WriteItemCommand {
	
	boolean insertItem(Book book);
	boolean deleteItem(Book book);

}
