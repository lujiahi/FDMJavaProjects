package com.fdmgroup.ElevatorProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;


public class Lift implements Runnable{
	private int id; 
	private boolean isIdle; 
	private int currentFloor; 
 	private Direction currentDirection; 
	private int currentDestination; 
	private TreeSet<Integer> stopFloors;
	private HashMap<Integer,Integer> loadRequest;
	private HashMap<Integer,Integer> unloadRequest;
	int rateOfSimulatedTime; 
	private boolean run = true;

	// List of state of the lift 
	enum Direction{
		UP,
		DOWN,
		STATIONARY,
	}
	
	// Constructor 
	// Each lift created is assumed to be at G/F and is stationary with no passengers and no 
	public Lift(int id) {
		this.id = id; 
		this.isIdle = true;
		this.currentFloor = 0;
		this.currentDirection = Direction.STATIONARY;
		this.currentDestination = 0; 
		this.stopFloors = new TreeSet<>();
		this.loadRequest = new HashMap<>();
		this.unloadRequest = new HashMap<>();
		this.rateOfSimulatedTime = Main.rateOfSimulatedTime;
	}
	
	public void run() {

		//System.out.println("Elevator " + this.id + " created");
		while (run) {
			try {
					// Checking if the
					while (loadRequest.size() != 0 || unloadRequest.size() != 0) {
						this.isIdle = false;
						if(this.currentFloor != this.currentDestination) {
							setNewDestinatedFloor();
							move();
							//System.out.println(this.currentDirection);
						} 
						else if (this.currentFloor == this.currentDestination) {
							if (loadRequest.containsKey(this.currentFloor)){
								load(this.currentFloor, loadRequest.get(this.currentFloor));
								setNewDestinatedFloor();
								move();
								//System.out.println(this.currentDirection);
							} 
							else if (unloadRequest.containsKey(this.currentFloor)) {
								unload(this.currentFloor,unloadRequest.get(this.currentFloor));
								setNewDestinatedFloor();
								move();
								//System.out.println(this.currentDirection);
								//System.out.println("Lift " + this.id+" Load request: "+ this.loadRequest + " UnloadRequest: " + this.unloadRequest);
							}
						} 
				} 
				if (loadRequest.size() == 0 && unloadRequest.size() == 0) {
					setIsIdle(true);
					setCurrentDirection(Direction.STATIONARY);
					//System.out.println("lift " + id + " is idle");
					Thread.sleep(rateOfSimulatedTime * 2);
				}

			} catch (InterruptedException e) {

					e.printStackTrace();
			}
		}
	}
	
	public void addRequest(Request request) {
		// Set the lift to be not idle
		setIsIdle(false);
		
		// Adding the loading request to loadRequest
		// If there are more than 1 request of loading from the same floor, then sums up the total number of passengers 
		if (this.loadRequest.containsKey(request.getCurrentFloor())){
			int num = this.loadRequest.get(request.getCurrentFloor());
			this.loadRequest.put(request.getCurrentFloor(), num + request.getNumPassengers());
		} else {
			this.loadRequest.put(request.getCurrentFloor(), request.getNumPassengers());
		}
		
		// Adding the unloading request to unloadRequest
		// If there are more than 1 request of loading from the same floor, then sums up the total number of passengers 
		if (this.unloadRequest.containsKey(request.getDestinationFloor())){
			int num = this.unloadRequest.get(request.getDestinationFloor());
			this.unloadRequest.put(request.getDestinationFloor(), num + request.getNumPassengers());
		} else {
			this.unloadRequest.put(request.getDestinationFloor(), request.getNumPassengers());
		}

		// adding the floors of the request to the stop list of the lift
		this.stopFloors.add(request.getCurrentFloor());
		this.stopFloors.add(request.getDestinationFloor());
		if (request.getCurrentFloor() > request.getDestinationFloor()) {
			Set<Integer> tempStopFloors1 = new TreeSet<>(Collections.reverseOrder());
			tempStopFloors1.addAll(this.stopFloors);
			this.stopFloors = (TreeSet<Integer>) tempStopFloors1;
		} else if (request.getCurrentFloor() < request.getDestinationFloor()){
			Set<Integer> tempStopFloors1 = new TreeSet<>();
			tempStopFloors1.addAll(this.stopFloors);
			this.stopFloors = (TreeSet<Integer>) tempStopFloors1;
		}
		
		// For self reference 
		//System.out.println("Lift " + this.id+" Load request: "+ this.loadRequest + " UnloadRequest: " + this.unloadRequest);
		
		// Setting new destination
		setNewDestinatedFloor();
	}	
	
	
	
	// Set the new destinated floor according to the list of stop floors (stopFloors)
	public void setNewDestinatedFloor() {
		
		// Checking if the lift is on one of the stops  
		if (stopFloors.size() != 0) {
			// Getting the first item of stop 
			Stream<Integer> tempStopFloors =  this.stopFloors.stream();
			int tempCurrentFloor = tempStopFloors.findFirst().get();
			
			// remove the current floor in the list of stop floors
			if (this.currentFloor == tempCurrentFloor && !this.loadRequest.containsKey(tempCurrentFloor) && !this.unloadRequest.containsKey(tempCurrentFloor)) {
				this.stopFloors.remove(this.currentFloor);
			} 
			
			// Set the new destination of the lift to the next stop if there's a new stop 
			if (stopFloors.size()!=0) {
				Stream<Integer> tempStopFloors1 =  this.stopFloors.stream();
				this.currentDestination = tempStopFloors1.findFirst().get();
			}
		}
	}

	
	
	public void move() throws InterruptedException {

		if (this.currentDestination > this.currentFloor) {
			
			// Set the direction and the number of floors to move
			setCurrentDirection(Direction.UP);
			int numMoveFloors = this.currentDestination - this.currentFloor;
			
			// Move the lift up for numMoveFloors and sleep for 3*rateOfSimulatedTime for each floor  
			for (int i = 0 ; i<numMoveFloors ; i++) {
				Thread.sleep(3*rateOfSimulatedTime);
				this.currentFloor ++;
			}	
			System.out.println("Elevator " + this.id+ " moved from "+ (this.currentDestination - numMoveFloors)+"/F up to "
			+ this.currentFloor + "/F.");
		}
		else if (this.currentDestination < this.currentFloor){
			// Set the direction and the number of floors to move 
			setCurrentDirection(Direction.DOWN);
			int numMoveFloors = this.currentFloor - this.currentDestination;
			
			// Move the lift down for numMoveFloors and sleep for 3*rateOfSimulatedTime for each floor
			for (int i=0; i<numMoveFloors; i++) {
				Thread.sleep(3*rateOfSimulatedTime);
				this.currentFloor--;
				
			}
			System.out.println("Elevator " + this.id+ " moved from "+ (this.currentDestination + numMoveFloors)+"/F down to "
					+ this.currentFloor + "/F.");
		
		}
	}
	
	// Print out the message of loading passengers and wait for 5*rateOfSimulatedTime
	// Then remove the load request from loadRequest 
	public void load(int loadFloor, int numPassengers) throws InterruptedException {
		Thread.sleep(5*rateOfSimulatedTime);
		System.out.println("Elevator " + this.id+" finish loading " + numPassengers + " passengers on " + loadFloor + "/F.");
		loadRequest.remove(loadFloor);
	}
	
	// Print out the message of unloading passengers and wait for 5*rateOfSimulatedTime
	// Then remove the unload request from unloadRequest
	public void unload(int unloadFloor, int numPassengers) throws InterruptedException {
		Thread.sleep(5*rateOfSimulatedTime);
		System.out.println("Elevator " + this.id+ " finish unloading " + numPassengers + " passengers on " + unloadFloor + "/F.");
		unloadRequest.remove(unloadFloor);
	}
	
	public void setStop() {
		this.run = false;
	}
	
	// Getters and Setters
	public int getId() {return this.id;}
	public boolean getIsIdle() {return this.isIdle;}
	public int getCurrentFloor() {return this.currentFloor;}
	public Direction getCurrentDirection() {return this.currentDirection;}
	public int getCurrentDestination() {return this.currentDestination;}
	public void setId(int id) {this.id = id;}
	public void setIsIdle(boolean isIdle) { this.isIdle = isIdle;}
	public void setCurrentFloor(int currentFloor) {this.currentFloor = currentFloor;}
	public void setCurrentDirection(Direction currentDirection) {this.currentDirection = currentDirection;}
	public void setCurrentDestination(int currentDestination) {this.currentDestination = currentDestination;}
	



	
}
