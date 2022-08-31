package currencyConverter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class ConverterTest {

	@Test
	@DisplayName("Convert 100USD to receive 88.48EUR")
	void test_Convert100USD_ToEUR() throws InvalidCurrencyException {
		double amountInEuro = Converter.convert("usd","eur",100.0);
		assertEquals(amountInEuro, 88.48);
	}
	
	@Test
	@DisplayName("Convert 100USD to receive 100USD")
	void test_Convert100USD_ToUSD() throws InvalidCurrencyException {
		double amountInUsd = Converter.convert("usd","usd",100.0);
		assertEquals(amountInUsd, 100.0);
	}
	
	@Test
	@DisplayName("Convert 100USD to receive 127.46CAD")
	void test_Convert100USD_ToCAD() throws InvalidCurrencyException{
		double amountInCad = Converter.convert("usd", "cad", 100.0);
		assertEquals(amountInCad, 127.46);
	}
	
	@Test
	@DisplayName("Convert 100ABC to throw an InvalidCurrencyException")
	void test_ConvertInvalidCurrency() {
		Exception e = assertThrows(InvalidCurrencyException.class, () -> {
			Converter.convert("abc","usd",100.0);
		});	
		assertEquals(e.getMessage(), "Invalid currency or invalid exchange rate.");
	}
}
