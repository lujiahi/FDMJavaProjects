package com.fdmgroup.ElevatorProject;

public class Request {
	private int currentFloor;
	private int destinationFloor;
	private int numPassengers;
	
	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}

	public void setDestinationFloor(int destinationFloor) {
		this.destinationFloor = destinationFloor;
	}

	public int getNumPassengers() {
		return numPassengers;
	}

	public void setNumPassengers(int numPassengers) {
		this.numPassengers = numPassengers;
	}
	
	public Request (int currentFloor, int destinationFloor, int numPassengers) {
		this.currentFloor = currentFloor;
		this.destinationFloor = destinationFloor;
		this.numPassengers = numPassengers;
	}
	
	// Update: implement toString() so that requests can be printed out
	@Override
	public String toString() {
		return "New request: " + numPassengers + " passenger(s) from " + currentFloor + "/F" + " request to go to " + destinationFloor + "/F.";
	}

}