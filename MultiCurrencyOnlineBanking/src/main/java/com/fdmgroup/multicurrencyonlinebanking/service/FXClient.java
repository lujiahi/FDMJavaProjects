package com.fdmgroup.multicurrencyonlinebanking.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.multicurrencyonlinebanking.model.FXConverter;

@Service
public class FXClient {
	
	// TODO: try use WebClient instead of HttpRequest and Jackson
	private ObjectMapper mapper = new ObjectMapper();

	// calling a currency converter API
	public double getResponse(String fromCurrency, String toCurrency, String amount) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.apilayer.com/exchangerates_data/convert?to=" + toCurrency + "&from=" + fromCurrency + "&amount=" + amount))
				.header("apikey", "O2MX8fqULIBJZiALNfaKGJ9DvvX7GQnw")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return parseJson(response.body());
	}
	
	// convert json into object and retrieve value using good old jackson
	public double parseJson(String json) {
		try {
			FXConverter fxConverter = mapper.readValue(json, FXConverter.class);
			return fxConverter.getResult();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
}
