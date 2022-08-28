package currencyConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Converter {

	public static double convert(String convertFrom, String convertTo, double amount) throws InvalidCurrencyException {
		
		BigDecimal amountInCAD = BigDecimal.ZERO;
		BigDecimal returnAmount = BigDecimal.ZERO;
		BigDecimal amountBD = BigDecimal.valueOf(amount);
		amountBD = amountBD.setScale(2, RoundingMode.HALF_UP);
		
		JSONReader jsonReader = new JSONReader();
		double exchangeRate1 = jsonReader.readFromRatesJSON("src/main/resources/rates.json", convertFrom);
		double exchangeRate2 = jsonReader.readFromRatesJSON("src/main/resources/rates.json", convertTo);
		BigDecimal exchangeRate1BD = BigDecimal.valueOf(exchangeRate1);
		BigDecimal exchangeRate2BD = BigDecimal.valueOf(exchangeRate2);
		
		if(convertFrom.equals("cad")) {
			amountInCAD = amountInCAD.add(amountBD);
		} else if(exchangeRate1 == 0) {
			throw new InvalidCurrencyException();
		} else {
			amountInCAD = amountBD.divide(exchangeRate1BD, 2, RoundingMode.HALF_UP);
		}
		
		amountInCAD = amountInCAD.setScale(2, RoundingMode.HALF_UP);
		
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
