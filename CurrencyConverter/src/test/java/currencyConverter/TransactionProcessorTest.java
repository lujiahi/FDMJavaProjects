package currencyConverter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;


class TransactionProcessorTest {

	@Test
	void testExecuteTransaction_UserFileReturnsNull() {
		assertFalse(TransactionProcessor.executeTransaction("src/main/resources/transactions.txt", "UserFileReturnsNull"));
	}
	
	@Test
	void testExecuteTransaction_InvalidTransactionFile() {
		assertFalse(TransactionProcessor.executeTransaction("InvalidTransactionFile", "src/main/resources/user.json"));
	}
	
	@Test
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
	void testExecuteTransaction_UserHas100USD_Converting100CADToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("usd", 100.0);
		String transactionDataString = "Jia cad usd 100";
		String[] transactionData = transactionDataString.split(" ");
		assertFalse(TransactionProcessor.processTransactionForUser(jia, transactionData));
	}
	
	@Test
	void testExecuteTransaction_UserHas100USD_Converting100USDToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("usd", 100.0);
		String transactionDataString = "Jia usd usd 100";
		String[] transactionData = transactionDataString.split(" ");
		TransactionProcessor.processTransactionForUser(jia, transactionData);
		assertEquals(jia.getWallet().get("usd"), 100.0);
	}
	
	@Test
	void testExecuteTransaction_UserHas20CAD100USD_Converting100CADToUSD() {
		User jia = new User("Jia", new HashMap<String, Double>());
		jia.getWallet().put("cad", 20.0);
		jia.getWallet().put("usd", 100.0);
		String transactionDataString = "Jia cad usd 100";
		String[] transactionData = transactionDataString.split(" ");
		assertFalse(TransactionProcessor.processTransactionForUser(jia, transactionData));
	}

}
