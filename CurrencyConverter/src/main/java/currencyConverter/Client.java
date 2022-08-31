package currencyConverter;

/**
 * Class used to run the currency converter
 * 
 * @author Lu Jia
 *
 */
public class Client {
	
	public static void main(String[] args) {
		TransactionProcessor.executeTransaction("src/main/resources/transactions.txt", "src/main/resources/users.json");
	}
}
