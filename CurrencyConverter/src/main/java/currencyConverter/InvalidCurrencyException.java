package currencyConverter;

public class InvalidCurrencyException extends Exception {
	
	public InvalidCurrencyException() {
		super("Invalid currency or invalid exchange rate.");
	}

}
