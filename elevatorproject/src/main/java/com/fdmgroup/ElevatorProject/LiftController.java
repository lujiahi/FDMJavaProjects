package com.fdmgroup.ElevatorProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.fdmgroup.ElevatorProject.Lift.Direction;

public class LiftController implements Runnable {

	public static final int NUMLIFTS = 3;
	private static final int REQUESTUPDATEINTERVAL = 1;
	private static List<Lift> lifts = new ArrayList<>();
	private static List<Thread> liftThreads = new ArrayList<>();
	private static Map<Integer, Request> upRequests = new HashMap<>(); // stores all the requests going up (integer represent the current floor)
	private static Map<Integer, Request> downRequests = new HashMap<>(); // stores all the requests going down
	private boolean run = true;


	// initialize the lifts, the threads and the jobs
	public static void initializeLifts() {
		for (int i = 1; i <= NUMLIFTS; i ++) {
			Lift newLift = new Lift(i);
			lifts.add(newLift);
			Thread liftThread = new Thread(newLift);
			liftThread.start();
			liftThreads.add(liftThread);
		}
	}

	public static void updateRequests() {
		List<Request> currentRequests = RequestManager.getListOfRequests();
		if(currentRequests != null && currentRequests.size() != 0) {
			int numRequests = currentRequests.size();
			for (int i = 0; i < numRequests; i++) {
				if (currentRequests.get(i).getDestinationFloor() > currentRequests.get(i).getCurrentFloor()) {
					upRequests.put(currentRequests.get(i).getCurrentFloor(), currentRequests.get(i));
				} else {
					downRequests.put(currentRequests.get(i).getCurrentFloor(), currentRequests.get(i));
				}
			}
			assignJobs(currentRequests);
		}
	}

	// assign jobs to the lifts
	public static void assignJobs(List<Request> currentRequests) {
		for (Lift lift: lifts) {
			// if the lift is going up, it will pick up passengers along the way
			if (lift.getCurrentDirection() == Direction.UP) {
				// loop through all the requests going up
				for(Map.Entry<Integer, Request> upRequest : upRequests.entrySet()) {
					Integer requestCurrentFloor = upRequest.getKey();
					Integer requestDestinationFloor = upRequest.getValue().getDestinationFloor();
					// if the request is at least 2 stops above the lift
					if(requestCurrentFloor >= lift.getCurrentFloor() + 2) {
						// assign request to the lift
						lift.addRequest(upRequest.getValue());
//						System.out.println("Controller: Elevator " + lift.getId() + " is assigned a request: " 
//						+ upRequest.getValue().getCurrentFloor() + "/F to " 
//						+ upRequest.getValue().getDestinationFloor() + "/F.");
//						upRequests.remove(upRequest);
						RequestManager.removeRequest(upRequest.getValue());
					}
				}
				// if the lift is going down, it will pick up passengers along the way
			} else if (lift.getCurrentDirection() == Direction.DOWN) {
				// loop through all the requests going down
				for(Map.Entry<Integer, Request> downRequest : downRequests.entrySet()) {
					Integer requestCurrentFloor = downRequest.getKey();
					Integer requestDestinationFloor = downRequest.getValue().getDestinationFloor();
					// if the request is at least 2 stops below the lift
					if(requestCurrentFloor <= lift.getCurrentFloor() - 2) {
						// assign request to the lift
						lift.addRequest(downRequest.getValue());
//						System.out.println("Controller: Elevator " + lift.getId() + " is assigned a request: " 
//						+ downRequest.getValue().getCurrentFloor() + "/F to " 
//						+ downRequest.getValue().getDestinationFloor()+ "/F.");
//						downRequests.remove(downRequest);
						RequestManager.removeRequest(downRequest.getValue());
					}
				}
				// if the lift is idle, it will find the nearest passenger to pick up
			} else if (lift.getIsIdle()) {
				int minDistance = Integer.MAX_VALUE;
				Request nearestRequest = null;
				// find the nearest passenger
				for(Request request : currentRequests) {
					int distance = Math.abs(lift.getCurrentFloor() - request.getCurrentFloor());
					if(distance < minDistance) {
						minDistance = distance;
						nearestRequest = request;
					}
				}
				// assign request to the lift
				if(nearestRequest != null) {
//					System.out.println("Controller: Elevator " + lift.getId() + 
//					" is assigned a request: " + nearestRequest.getCurrentFloor() + "/F to " 
//					+ nearestRequest.getDestinationFloor()+ "/F.");
					lift.addRequest(nearestRequest);
					RequestManager.removeRequest(nearestRequest);
					if(nearestRequest.getDestinationFloor() > nearestRequest.getCurrentFloor()) {
						upRequests.remove(nearestRequest.getCurrentFloor(), nearestRequest);
					} else {
						downRequests.remove(nearestRequest.getCurrentFloor(), nearestRequest);
					}
				}

			}
		}
	}

	@Override
	public void run() {
		LiftController.initializeLifts();
		while(run) {
			updateRequests();
			try {
				Thread.sleep(REQUESTUPDATEINTERVAL * Main.rateOfSimulatedTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setStop() {
		this.run = false;
	}

	public void setLiftStop() {
		for (Lift lifts : lifts) {
			lifts.setStop();
		}
	}

}
