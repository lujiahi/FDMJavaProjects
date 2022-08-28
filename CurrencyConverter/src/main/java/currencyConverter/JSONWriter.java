package currencyConverter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JSONWriter {
	private static final Logger logger = LogManager.getLogger();
		
	public void writeToUserJSON(String fileName, List<User> userList) {
		
		ObjectMapper mapper = new ObjectMapper();	
		// Make the protected fields in User class visible to the ObjectMapper
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		
		try {
			writer.writeValue(new File(fileName), userList);	
		} catch (StreamWriteException e) {
			logger.error(e.getMessage());
		} catch (DatabindException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
