package currencyConverter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class TransactionProcessorTest {

	@Test
	@DisplayName("ExecuteTransaction returns false if the user file is invalid")
	void testExecuteTransaction_UserFileReturnsNull() {
		assertFalse(TransactionProcessor.executeTransaction("src/main/resources/transactions.txt", "UserFileReturnsNull"));
	}
	
	@Test
	@DisplayName("ExecuteTransaction returns false if the transaction file is invalid")
	void testExecuteTransaction_InvalidTransactionFile() {
		assertFalse(TransactionProcessor.executeTransaction("InvalidTransactionFile", "src/main/resources/user.json"));
	}
	
	@Test
	@DisplayName("User starts with 100USD and 120CAD. Converts 100CAD to USD. User ends up with 178.45USD and 20CAD.")
	void testExecuteTransaction_UserHas100USD120CAD_Converting100CADToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("usd", 100.0);
		jia.getWallet().put("cad", 120.0);
		String transactionDataString = "Jia cad usd 100";
		String[] transactionData = transactionDataString.split(" ");
		TransactionProcessor.processTransactionForUser(jia, transactionData);
		assertEquals(jia.getWallet().get("usd"), 178.45);
		assertEquals(jia.getWallet().get("cad"), 20.0);
	}
	
	@Test
	@DisplayName("User starts with 120CAD. Converts 100CAD to USD. User ends up with 78.45USD and no CAD.")
	void testExecuteTransaction_UserHas100CAD_Converting100CADToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("cad", 100.0);
		String transactionDataString = "Jia cad usd 100";
		String[] transactionData = transactionDataString.split(" ");
		TransactionProcessor.processTransactionForUser(jia, transactionData);
		assertEquals(jia.getWallet().get("usd"), 78.45);
		assertEquals(jia.getWallet().get("cad"), 0.0);
	}
	
	@Test
	@DisplayName("User starts with 100USD. Converts 100USD to USD. Ends up with 100USD.")
	void testExecuteTransaction_UserHas100USD_Converting100USDToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("usd", 100.0);
		String transactionDataString = "Jia usd usd 100";
		String[] transactionData = transactionDataString.split(" ");
		TransactionProcessor.processTransactionForUser(jia, transactionData);
		assertEquals(jia.getWallet().get("usd"), 100.0);
	}
	
	@Test
	@DisplayName("User starts with 100USD. Converts 100CAD to JPY. Transaction fails.")
	void testExecuteTransaction_UserHas100USD_Converting100CADToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("usd", 100.0);
		String transactionDataString = "Jia cad jpy 100";
		String[] transactionData = transactionDataString.split(" ");
		assertFalse(TransactionProcessor.processTransactionForUser(jia, transactionData));
	}

	@Test
	@DisplayName("User starts with 20CAD and 100USD. Converts 100CAD to USD. Transaction fails.")
	void testExecuteTransaction_UserHas20CAD100USD_Converting100CADToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("cad", 20.0);
		jia.getWallet().put("usd", 100.0);
		String transactionDataString = "Jia cad usd 100";
		String[] transactionData = transactionDataString.split(" ");
		assertFalse(TransactionProcessor.processTransactionForUser(jia, transactionData));
	}

}
