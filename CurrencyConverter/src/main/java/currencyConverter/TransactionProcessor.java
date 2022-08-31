package currencyConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that reads transaction data from a file and processes the transactions
 * 
 * @author Lu Jia
 *
 */
public class TransactionProcessor {
	private static final Logger logger = LogManager.getLogger();

	/**
	 * Method to read transaction data from the transaction file and user data from a user file
	 * Calls processTransactionForUser() to further process transaction for each user
	 * Calls updateUserFile() to update the user file
	 * 
	 * @param transactionFileName
	 * @param userFileName
	 * @return
	 */
	public static boolean executeTransaction(String transactionFileName, String userFileName) {
		
		JSONReader jsonReader = new JSONReader();
		
		// read user list from user.json		
		List<User> users = jsonReader.readFromUserJSON(userFileName);
		if(users == null) {
			logger.error("Could not read users from the user file.");
			return false;
		}	
		// scan the transaction.txt file line by line
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(transactionFileName));
			
			boolean emptyTransactionFile = true;
			
			while(scanner.hasNextLine()) {
				emptyTransactionFile = false;
				
				// read transaction data
				String[] transactionData = scanner.nextLine().split(" ");
				String name = transactionData[0];
				
				boolean userFound = false;
				
				// call processTransactionForUser and pass in the user and transaction data
				for(User user : users) {
					if(user.getName().equals(name)) {
						userFound = true;			
						processTransactionForUser(user, transactionData);
					}
				}				
				if(!userFound) {
					logger.warn("User not found. Transaction aborted.");
				}				
			}			
			if(emptyTransactionFile) {
				logger.error("Transaction file is empty.");
				return false;
			}
			// update user.json
			updateUserFile(userFileName, users);
			
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}		
		return true;
	}

	/**
	 * Method to process currency conversion for a particular user
	 * 
	 * @param user
	 * @param transactionData
	 * @return
	 */
	static boolean processTransactionForUser(User user, String[] transactionData) {
		
		String name = transactionData[0];
		String convertFrom = transactionData[1];
		String convertTo = transactionData[2];
		double amount = Double.parseDouble(transactionData[3]);
		
		// check if the user has any wallet
		if(user.getWallet() == null) {
			logger.warn(name + "does not have any wallet. Transaction aborted.");
			return false;
		}
		
		logger.info(name + "'s initial wallet: " + user.getWallet());
		logger.info(String.format("%s wants to convert %.2f %s to %s", name, amount, convertFrom, convertTo));
		
		// get the current balance in user's wallet for the currency that he wants to convert from
		double amountInWallet = user.getAmountInWallet(convertFrom);
		logger.info(String.format("%s's balance in %s: %.2f", name, convertFrom, amountInWallet));
		
		// check if user has enough balance in the wallet
		if(amountInWallet >= amount) {
			logger.info(String.format("Attempting conversion for %s", name));
			logger.info(String.format("Converting %.2f %s...", amount, convertFrom));
			
			boolean conversionSuccessful = false;
			
			try {
				// attempt the conversion
				double amountGet = Converter.convert(convertFrom, convertTo, amount);
				conversionSuccessful = true;
				logger.info(String.format("Into %.2f %s according to today's exchange rate", amountGet, convertTo));
				
				// debit from the convertFrom wallet
				user.deductAmountFromWallet(convertFrom, amount);					
				
				// credit into the convertTo wallet
				user.addAmountToWallet(convertTo, amountGet);
				
			} catch(InvalidCurrencyException e) {
				logger.error(e.getMessage());
				return false;
			}
			
			if(conversionSuccessful) {
				logger.info(user.getName() + "'s updated wallet: " + user.getWallet());
			}
			
		} else {
			logger.warn(String.format("%s does not have enought balance in %s. Transaction aborted.", name, convertFrom));
			return false;
		}
		return true;
	}

	/**
	 * Method to update the user file by passing in an updated list of users
	 * 
	 * @param fileName
	 * @param user
	 */
	static void updateUserFile(String fileName, List<User> user) {
		JSONWriter jsonWriter = new JSONWriter();
		jsonWriter.writeToUserJSON(fileName, user);
	}
	
}
