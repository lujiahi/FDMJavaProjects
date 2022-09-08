package com.fdmgroup.ElevatorProject;

import java.util.ArrayList;

public class RequestManager {
	
	private static ArrayList<Request> listOfRequests = new ArrayList<Request>();
	
	public static ArrayList<Request> getListOfRequests() {
		return listOfRequests;
	}

	public static void setListOfRequests(ArrayList<Request> listOfRequests) {
		RequestManager.listOfRequests = listOfRequests;
	}

//	public static ArrayList<Request> requestList () {
//		System.out.println(listOfRequests.size());
//		return listOfRequests;
//	}
	
	public static void addRequest (Request request) {
		listOfRequests.add(request);
	}
	
	public static void removeRequest(Request request) {
		listOfRequests.remove(request);
	}

}
