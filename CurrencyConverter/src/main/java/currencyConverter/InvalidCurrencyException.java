package currencyConverter;

/**
 * InvalidCurrencyException
 * 
 * @author Lu Jia
 *
 */
public class InvalidCurrencyException extends Exception {
	
	/**
	 * Constructor for the custom InvalidCurrencyException
	 */
	public InvalidCurrencyException() {
		super("Invalid currency or invalid exchange rate.");
	}

}
