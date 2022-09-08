package com.fdmgroup.ElevatorProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	static int totalSimulatedTime = 0;
	static int rateOfSimulatedTime = 0;
	
	//Update: added some constants
	static final int REQUESTINTERVAL = 10;

	public static void main(String[] args) throws FileNotFoundException {
	
		//Initialise file and scanner for totalSimulatedTime and rateOfSimulatedTime
		File fileInput = new File("ElevatorConfig.txt");
		Scanner scanner1 = new Scanner(fileInput);
		//In a try and finally clause to remove resource leak
		try { 
			//lineNumber1 refers to each line in the txt file
			int lineNumber1 = 1;
			while(scanner1.hasNextLine() && lineNumber1 <=3){
				//puts each line into variable line
				String line1 = scanner1.nextLine();
				//Saves total simulation time into variable
				if (lineNumber1 == 1) {
					totalSimulatedTime = Integer.parseInt(line1);
				    System.out.println("Total time of simulation: " + totalSimulatedTime + " simulated seconds");
				}
				//Saves rate of simulated time into variable
				else if (lineNumber1 == 3) {
					rateOfSimulatedTime = Integer.parseInt(line1);
				    System.out.println("Rate of each simulated second: " + rateOfSimulatedTime + " milliseconds");
		            System.out.println();
		            System.out.println("-------------------------Elevator Simulation Starts--------------------------");
		            System.out.println();
				}
				lineNumber1++;
			}
		} finally {
			scanner1.close();
		}
						

		//Initialise timer
		Timer timer = new Timer();

		//initialize the lifts
		LiftController lc = new LiftController();
		Thread liftController = new Thread(lc);
		liftController.start();
		
		
		//Running of timertask 
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
            timer.cancel();
            System.out.println();
            System.out.println((totalSimulatedTime) + " simulated seconds have passed");
            lc.setStop();
            System.out.println("---------------------Liftcontroller terminated---------------------");
            lc.setLiftStop();
            System.out.println("-------------------------Lifts terminated--------------------------");
            }
		}, (totalSimulatedTime*rateOfSimulatedTime));
		
		//Initialise file and scanner for requests
		Scanner scanner = new Scanner(fileInput);
		//In a try and finally clause to remove resource leak
		try { 
			//lineNumber refers to each line in the txt file
			int lineNumber = 1;
        	while(scanner.hasNextLine()){
        		//puts each line into variable line
        		String line = scanner.nextLine();
        		if (lineNumber >= 5) {
        			//skips empty lines
        			if (line.isEmpty()) {
        				continue;
        			}
        			//Saves the currentFloor, destinationFloor and numPassengers into request
        			//Append request into arraylist
        			else {
        				//System.out.println(line);
        				String[] lineSplit = line.split(" ");
        				if (lineSplit[0].equals("G")) {
        					lineSplit[0] = "0";
        				}
        				else if (lineSplit[1].equals("G")) {
        					lineSplit[1] = "0";
        				}
        				int currentFloor = Integer.parseInt(lineSplit[0]); 
        				int destinationFloor = Integer.parseInt(lineSplit[1]); 
        				int numPassengers = Integer.parseInt(lineSplit[2]); 
        				Request request = new Request(currentFloor, destinationFloor, numPassengers);
        			
        				// Update: Add request to the RequestManager
        				RequestManager.addRequest(request);
        				
        				// Update: Print out the request:
        				System.out.println(request);
        				
        				//Sleep for 1 simulated second after each request
        				try {
							Thread.sleep(REQUESTINTERVAL * rateOfSimulatedTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						};
        			}
        		}
        		lineNumber++;
        	} 
		} finally {
			scanner.close();
		}
	}

}
