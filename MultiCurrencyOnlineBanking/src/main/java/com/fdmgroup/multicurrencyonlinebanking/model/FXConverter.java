package com.fdmgroup.multicurrencyonlinebanking.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

// the object that is mapped to the json retrieved from an FX API
public class FXConverter {
	
	private String success;
	private Map<String, String> query;
	private Map<String, Double> info;
	private Date date;
	private double result;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public Map<String, String> getQuery() {
		return query;
	}
	public void setQuery(Map<String, String> query) {
		this.query = query;
	}
	public Map<String, Double> getInfo() {
		return info;
	}
	public void setInfo(Map<String, Double> info) {
		this.info = info;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}
	public FXConverter() {
		super();
	}
	public FXConverter(String success, Map<String, String> query, Map<String, Double> info, Date date, double result) {
		super();
		this.success = success;
		this.query = query;
		this.info = info;
		this.date = date;
		this.result = result;
	}

}
