package semester_aufgabe;

import java.util.ArrayList;
import java.util.Scanner;
import semester_aufgabe.vec2f;
import aufgaben_5.Tools;

public class Snake {
	
	public static void main(String[] args) {
		Scanner inputscanner = new Scanner(System.in);
		
		vec2f dimensions = new vec2f(0,0);
		
		System.out.println("##############################");
		System.out.println("# Snake Console Application  #");
		System.out.println("##############################");
		
		System.out.println("Enter board size to start:");
		
		
		try {
		System.out.print("width:");
		dimensions.x = Integer.parseInt(inputscanner.next());
		
		System.out.print("height:");
		dimensions.y = Integer.parseInt(inputscanner.next());
		} catch (NumberFormatException exc) {
			System.out.println("invalid input. try whole positive integers. exiting...");
			inputscanner.close();
			return;
		}
		
		if ( dimensions.x < 21 || dimensions.y < 21 || dimensions.x > 2 || dimensions.y > 2 ) // maximum size of 20x20, minimum size of 3x3
			Mainloop(inputscanner, dimensions);
		else
			System.out.println("invalid bounds for board size. exiting...");
		inputscanner.close();
	}
	
	private static vec2f RelocateApple(vec2f dimensions) {

		apple_location.x = Tools.createRandomInt(0, (int)(dimensions.x - 1));
		apple_location.y = Tools.createRandomInt(0, (int)(dimensions.y - 1));
		
		for (vec2f segment : snake_segments)
			if (segment.equals(apple_location) || snake_location.equals(apple_location))
				return RelocateApple(dimensions);
		return apple_location;
	}
	
	private static char GetInput(Scanner inputscanner) {
		
		System.out.println("input keys: 'w' for up, 'a' for left, 's' for down, 'd' for right or 'q' to quit");
		String input_raw = inputscanner.next();
		char input = input_raw.charAt(0);
		
		if (input == 'w' || input == 'a' || input == 's' || input == 'd' || input == 'q') {
			return input;
		}
		else {
			System.out.println(input + " is an invalid input, try something else.");
			return GetInput(inputscanner);
		}
	}
	
	private static boolean UpdateGameState(char input, vec2f dimensions) {
	
		// core game logic
		// keeping track of where the player is and what game state that would lead to 
		
		if (snake_segments.size() != 0 && snake_segments.size() > snake_length)
			snake_segments.removeFirst();
		
		if (snake_segments.size() <= snake_length)
			snake_segments.add(new vec2f(snake_location));
		
		// interpret inputs
		if (input == 'q') {
			game_over_info = 2;
			return true;	
		}
		else if (input == 'w')
			snake_location.y--;
		else if (input == 'a')
			snake_location.x--;
		else if (input == 's')
			snake_location.y++;
		else if (input == 'd')
			snake_location.x++;
		
		
		// check if all segments + head fill the plane
		if (dimensions.x * dimensions.y == snake_segments.size() + 1) {
			game_over_info = 1;
			return true;
		}
		// apple relocation when snake touches apple and only when player has not won (would result in endless tries of relocation)
		// relocate until apple is on free space
		else if (apple_location.equals(snake_location)) {
				snake_length++;
				RelocateApple(dimensions);
		}

		// GAME OVER EVENTS
		
		// check if player is out of bounds
		if (snake_location.x < 0 || snake_location.x > dimensions.x - 1 ||
			snake_location.y < 0 || snake_location.y > dimensions.y - 1)
		{
			return true;
		}
		
		// check if player is touching one of the segments
		for (vec2f segment : snake_segments)
			if (segment.equals(snake_location)) {
				return true;
			}
		
		return false;
	}
	
	private static void Render(vec2f dimensions) {
		
		// print top border
		System.out.print("#");
		for (int i = 0; i < dimensions.x; i++)
			System.out.print("##");
		System.out.print("#\n");
		
		// print board
		for (int i = 0; i < dimensions.y; i++) {
			System.out.print("#");
			for (int j = 0; j < dimensions.x; j++) {
				
				boolean issegment = false;
				for (vec2f segment : snake_segments)
					if (segment.equals(new vec2f(j,i)))
						issegment = true;
				
				if (new vec2f(j,i).equals(snake_location))
					System.out.print(" O");
				else if (new vec2f(j,i).equals(apple_location))
					System.out.print(" A");
				else if (issegment)
					System.out.print(" S");
				else
					System.out.print("  ");
			}
			System.out.print("#\n");
		}
		// print bottom border
		System.out.print("#");
		for (int i = 0; i < dimensions.x; i++)
			System.out.print("##");
		System.out.print("#\n");
		
	}
	
	static private ArrayList<vec2f> snake_segments = new ArrayList<vec2f>();
	static private int snake_length = 3;
	
	static private vec2f snake_location = new vec2f(0,0);
	static private vec2f apple_location;
	static private int game_over_info = 0;
	
	private static void Mainloop(Scanner inputscanner, vec2f dimensions) {
		
		apple_location = new vec2f(dimensions.x - 1, dimensions.y - 1);
		char input = 'd';
		
		boolean break_loop = false;
		
		while (!break_loop) {

			input = GetInput(inputscanner);
			break_loop = UpdateGameState(input, dimensions);
			
			Render(dimensions);

		}
		if (game_over_info == 0) {
			System.out.println("##############################");
			System.out.println("# GAME OVER - YOU LOST       #");
			System.out.println("##############################");
		}
		else if (game_over_info == 1) {
			System.out.println("##############################");
			System.out.println("# GAME OVER - YOU WON        #");
			System.out.println("##############################");
		}
		else if (game_over_info == 2) {
			System.out.println("##############################");
			System.out.println("# GAME OVER - QUIT BY PLAYER #");
			System.out.println("##############################");
		}
			
	}
}
