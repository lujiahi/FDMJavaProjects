package hangman;

/**
 * Class to create a simple console version of the Hangman game. Word is randomly picked from a pre-populated word list.
 * 
 * @author Lu Jia
 * 
 */

import java.util.Scanner;

public class Hangman {
	
	private String hiddenWord;
	private static int totalGuesses = 8; 
	private static String[] wordList = {"APPLE", "BREAKFAST", "COMPUTER", "DOG", "EGGPLANT", "FIREMAN", "GUESTROOM", 
			"HAPPINESS", "ICECREAM", "JELLY", "LADDER", "KANGAROO", "MONEY", "NONSENSE", "OPPOSITE", "PINEAPPLE", 
			"QUEEN", "RESPECT", "STARFISH", "TURTLE", "UNION", "VIKINGS", "WATERMELON", "XRAY", "YELLOW", "ZOOLOGY"};

	/**
	 * Constructor to initialize a new game.
	 */
	public Hangman() {
		int randomIndex = (int) (Math.random() * wordList.length);
		this.hiddenWord = wordList[randomIndex];
	}
	
	/**
	 * Method to implement the game logic. 
	 */
	public void play() {
		
		System.out.println("Welcome to Hangman!");	
		int numGuessesLeft = totalGuesses;
		String previousGuesses = "";
		Scanner input = new Scanner(System.in);
		
		// Hide all the letters as "-" initially
		StringBuilder lettersRevealed = new StringBuilder();
		for(int i = 0; i < this.hiddenWord.length(); i++) {
			lettersRevealed.append("-");
		}

		while(numGuessesLeft > 0) {
			System.out.println("The word now looks like this: " + lettersRevealed);
			if(numGuessesLeft == 1) {
				System.out.println("You have only " + numGuessesLeft + " guess left.");
			}
			else {
				System.out.println("You have " + numGuessesLeft + " guesses left.");
			}
			System.out.print("Your guess: ");
				
			// Only allow a single letter as the input
			if(input.hasNext("[a-zA-Z]")) {
				char guess = (char) input.next().toUpperCase().charAt(0);
				
				// Check whether letter has been entered previously
				if(previousGuesses.indexOf(guess)!=-1){
					System.out.println("You've already guessed this letter. Please pick another.");
				}
				else {
					previousGuesses += guess;

					boolean correct = false;
					for(int i = 0; i < this.hiddenWord.length(); i++) {
						if(this.hiddenWord.charAt(i) == guess) {
							lettersRevealed.setCharAt(i, guess);
							correct = true;
						}
					}
					if(!correct) {
						System.out.println("There are no " + guess + "'s in the word.");
						numGuessesLeft --;
					}
					else {
						System.out.println("That guess is correct.");
						if(lettersRevealed.toString().equals(this.hiddenWord)) {
							System.out.println("You guessed the word: " + this.hiddenWord);
							System.out.println("You win.");
							break;
						}
					}
				}
			}
			else {
				// Empty the input stream to avoid an infinite loop.
				input.next();
				System.out.println("Invalid input. Please enter a single letter.");
			}
		}
		
		if(!lettersRevealed.toString().equals(this.hiddenWord)) {
			System.out.println("You're completely hung.");
			System.out.println("The word was: " + this.hiddenWord);
			System.out.println("You lose.");
		}
		
		input.close();
	}
}