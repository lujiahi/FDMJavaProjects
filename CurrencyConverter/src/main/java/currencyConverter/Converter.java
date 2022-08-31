package currencyConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class to convert some amount in one currency to another currency
 * 
 * @author Lu Jia
 *
 */
public class Converter {
	
	/**
	 * Method to convert some amount in one currency to another currency. The actual exchange rates are read from a JSON file. 
	 * All exchange rates from the file are based off CAD.
	 * 
	 * @param convertFrom
	 * @param convertTo
	 * @param amount
	 * @return
	 * @throws InvalidCurrencyException
	 */
	public static double convert(String convertFrom, String convertTo, double amount) throws InvalidCurrencyException {
		
		// use BigDecimal to achieve high precision
		BigDecimal amountInCAD = BigDecimal.ZERO;
		BigDecimal returnAmount = BigDecimal.ZERO;
		BigDecimal amountBD = BigDecimal.valueOf(amount);
		amountBD = amountBD.setScale(2, RoundingMode.HALF_UP);
		
		// read rates from the file by calling a JSON reader
		JSONReader jsonReader = new JSONReader();
		double exchangeRate1 = jsonReader.readFromRatesJSON("src/main/resources/rates.json", convertFrom);
		double exchangeRate2 = jsonReader.readFromRatesJSON("src/main/resources/rates.json", convertTo);
		BigDecimal exchangeRate1BD = BigDecimal.valueOf(exchangeRate1);
		BigDecimal exchangeRate2BD = BigDecimal.valueOf(exchangeRate2);
		
		// convert currency to CAD first
		// skip convention if the currency to convert from is already CAD
		// this step is also needed because JSON file does not contain any exchange rate for cad
		// also check if the exchange rate returned is 0
		if(convertFrom.equals("cad")) {
			amountInCAD = amountInCAD.add(amountBD);
		} else if(exchangeRate1 == 0) {
			throw new InvalidCurrencyException();
		} else {
			amountInCAD = amountBD.divide(exchangeRate1BD, 2, RoundingMode.HALF_UP);
		}
		
		// round result to 2 decimal place
		amountInCAD = amountInCAD.setScale(2, RoundingMode.HALF_UP);
		
		// convert currency to the destination currency
		if(convertTo.equals("cad")) {
			returnAmount = amountInCAD;
		} else if(exchangeRate2 == 0) {
			throw new InvalidCurrencyException();
		} else {
			returnAmount = amountInCAD.multiply(exchangeRate2BD);
		}
		
		returnAmount = returnAmount.setScale(2, RoundingMode.HALF_UP);
				
		return returnAmount.doubleValue();
	}

}
