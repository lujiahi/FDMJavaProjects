package currencyConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that represents bank users and their wallet
 * 
 * @author Lu Jia
 *
 */
public class User {
	private static final Logger logger = LogManager.getLogger();
	
	private String name;
	private Map<String, Double> wallet;

	/**
	 * The default constructor
	 */
	protected User() {

	};
	
	/**
	 * The parameterized constructor
	 * 
	 * @param name
	 * @param wallet
	 */
	protected User(String name, Map<String, Double> wallet) {
		this.name = name;
		this.wallet = wallet;
	}

	/**
	 * 
	 * @return
	 */
	protected String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	protected Map<String, Double> getWallet() {
		return wallet;
	}

	/**
	 * 
	 * @param wallet
	 */
	protected void setWallet(Map<String, Double> wallet) {
		this.wallet = wallet;
	}
	
	/**
	 * Method that returns the current balance for a particular currency
	 * 
	 * @param currency
	 * @return
	 */
	protected double getAmountInWallet(String currency) {
		if(wallet.containsKey(currency)) {
			return wallet.get(currency);
		} else {
			logger.warn(name + " does not have any " + currency);
		}
		return 0.0;
	}
	
	/**
	 * Method that adds an amount in a particular currency to the wallet
	 * 
	 * @param currency
	 * @param toAdd
	 */
	protected void addAmountToWallet(String currency, double toAdd) {
		BigDecimal toAddBD = new BigDecimal(toAdd);
		
		if(wallet.containsKey(currency)) {
			BigDecimal amountInWallet = new BigDecimal(wallet.get(currency));
			amountInWallet = amountInWallet.add(toAddBD);
			amountInWallet = amountInWallet.setScale(2, RoundingMode.HALF_UP);
			wallet.put(currency, amountInWallet.doubleValue());
		} else {
			wallet.put(currency, toAddBD.doubleValue());
		}
	}
	
	/**
	 * Method that tries to deduct an amount of a particular currency from the wallet
	 * 
	 * @param currency
	 * @param toDeduct
	 * @return
	 */
	protected boolean deductAmountFromWallet(String currency, double toDeduct) {
	
		if(wallet.containsKey(currency)) {
			double amountInWallet = wallet.get(currency);
			if(amountInWallet >= toDeduct) {
				BigDecimal amountInWalletBD = new BigDecimal(amountInWallet);
				BigDecimal toDeductBD = new BigDecimal(toDeduct);
				BigDecimal balance = amountInWalletBD.subtract(toDeductBD);
				balance = balance.setScale(2, RoundingMode.HALF_UP);
				wallet.put(currency, balance.doubleValue());
				return true;
			} else {
				logger.error(name + " does not have enough " + currency);
				return false;
			}
		} else {
			logger.error(name + " does not have any " + currency);
			return false;
		}
	}

}
