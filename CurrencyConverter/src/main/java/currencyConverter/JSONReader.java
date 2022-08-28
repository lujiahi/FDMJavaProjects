package currencyConverter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONReader {
	private static final Logger logger = LogManager.getLogger();

	public List<User> readFromUserJSON(String fileName) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			User[] users = mapper.readValue(new File(fileName), User[].class);
			return Arrays.asList(users);
		} catch (StreamReadException e) {
			logger.error(e.getMessage());
		} catch (DatabindException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}	
		return null;
	}
	
	public double readFromRatesJSON(String fileName, String currency) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			JsonNode node = mapper.readTree(new File(fileName));
			if(node.get(currency) != null) {
				return node.get(currency).get("rate").doubleValue();
			}
		} catch (StreamReadException e) {
			logger.error(e.getMessage());
		} catch (DatabindException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return 0.0;
	}

}
