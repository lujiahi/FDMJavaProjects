package currencyConverter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
	
	// initialize a default user and a new wallet
	User jia;
	Map<String, Double> wallet = new HashMap<>();
	
	// instantiate the user
	@BeforeEach
	void init() {
		jia = new User("Jia", wallet);
	}

	@Test
	@DisplayName("Successfully creates a user named Jia with a wallet that contains 100USD and 250.5SGD.")
	void testCreateUser() {
		wallet.put("usd", 100.0);
		wallet.put("sgd", 250.5);
		assertEquals(jia.getName(), "Jia");
		assertEquals(jia.getWallet().get("usd"), 100.0);
		assertEquals(jia.getWallet().get("sgd"), 250.5);
	}
	
	@Test
	@DisplayName("User has only 180EUR in her wallet. She has 0USD.")
	void testGetAmountInWallet_EmptyWallet() {
		wallet.put("eur", 180.0);
		double usdAmount = jia.getAmountInWallet("usd");
		assertEquals(usdAmount, 0.0);
	}
	
	@Test
	@DisplayName("User has 300USD in her wallet. She has 300USD.")
	void testGetAmountInWallet_100USD() {
		wallet.put("usd", 300.0);
		double usdAmount = jia.getAmountInWallet("usd");
		assertEquals(usdAmount, 300.0);
	}
	
	@Test
	@DisplayName("User has 1CAD in her wallet. Add 140CAD and now she has 141CAD.")
	void testAddAmountToWallet_140CAD() {
		wallet.put("cad", 1.0);
		jia.addAmountToWallet("cad", 140.0);
		assertEquals(wallet.get("cad"), 141.0);
	}
	
	@Test
	@DisplayName("User has empty wallet. Add 10000JPY and now she has 10000JPY.")
	void testAddAmountToWallet_NoWallet_CreateWallet() {
		jia.addAmountToWallet("jpy", 10000.0);
		assertEquals(wallet.get("jpy"), 10000.0);
	}
	
	@Test
	@DisplayName("User has 500USD in her wallet. Deduct 250USD and now she has 250USD.")
	void testDeductAmountInWallet_Deduct250USD_From500USD() {
		wallet.put("usd", 500.0);
		jia.deductAmountFromWallet("usd", 250.0);
		assertEquals(wallet.get("usd"), 250.0);
	}
	
	@Test
	@DisplayName("User has 200USD in her wallet. She can't deduct 250USD from her wallet.")
	void testDeductAmountInWallet_Deduct250USD_From200USD() {
		wallet.put("usd", 200.0);
		assertFalse(jia.deductAmountFromWallet("usd", 250.0));
	}

}
