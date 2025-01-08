package semester_aufgabe;

import java.util.ArrayList;
import java.util.Scanner;
import semester_aufgabe.Point2D;
import aufgaben_5.Tools;

public class Snake 
{
	/*
	 * Variablen zum steuern des Spiels
	 */
	static private ArrayList<Point2D> snake_segments = new ArrayList<Point2D>();
	static private ArrayList<Point2D> free_spaces = new ArrayList<Point2D>();
	
	static private Point2D snake_location = new Point2D(2,0);
	static private Point2D apple_location;
	static private int game_over_info = 0;
	/*
	 * Main-Funktion
	 * - lässt den Spieler die Spielfeldgröße eingeben
	 * - startet bei validen Eingaben das Spiel über Mainloop()
	 */
	public static void main(String[] args) {
		Scanner inputscanner = new Scanner(System.in);
		
		Point2D dimensions = new Point2D(0,0);
		
		System.out.println("##############################");
		System.out.println("# Snake Console Application  #");
		System.out.println("##############################");
		
		System.out.println("Enter board size to start (max: 20x20, min: 4x4):");
		
		
		try 
		{
		System.out.print("width:");
		dimensions.x = Integer.parseInt(inputscanner.next());
		// maximum size of 20x20, minimum size of 4x4
		if (dimensions.x > 20 || dimensions.x < 4)
			throw new NumberFormatException();
		
		System.out.print("height:");
		dimensions.y = Integer.parseInt(inputscanner.next());
		// maximum size of 20x20, minimum size of 4x4
		if (dimensions.y > 20 || dimensions.y < 4 )
			throw new NumberFormatException();
		} 
		catch (NumberFormatException exc) 
		{
			System.out.println("invalid input. exiting...");
			inputscanner.close();
			return;
		}
		

		Mainloop(inputscanner, dimensions);

		inputscanner.close();
	}
	/*
	 * setzt den Apfel auf zufällige und valide (nicht vom Spieler besetzte) Position auf dem Spielbrett.
	 */
	private static void RelocateApple(Point2D dimensions) 
	{
		apple_location = new Point2D(free_spaces.get(Tools.createRandomInt(0, free_spaces.size() - 1)));
	}
	/*
	 * parsed die Eingabe
	 */
	private static char GetInput(Scanner inputscanner) 
	{
		
		System.out.println("input keys: 'w' for up, 'a' for left, 's' for down, 'd' for right or 'q' to quit");
		System.out.println("then hit enter to continue.");
		String input_raw = inputscanner.next();
		char input = input_raw.charAt(0);
		
		if (input == 'w' || input == 'a' || input == 's' || input == 'd' || input == 'q') 
		{
			return input;
		}
		else 
		{
			System.out.println(input + " is an invalid input, try something else.");
			return GetInput(inputscanner);
		}
	}
	/*
	 * passt das Spiel an Veränderungen durch Eingaben an
	 */
	private static boolean UpdateGameState(char input, Point2D dimensions) 
	{
	
		Point2D snake_before_move = new Point2D(snake_location);
		// interpret inputs
		if (input == 'q') 
		{
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
		
		// GAME OVER EVENTS
		
		// check if player is out of bounds
		if (snake_location.x < 0 || snake_location.x > dimensions.x - 1 ||
			snake_location.y < 0 || snake_location.y > dimensions.y - 1)
		{
			return true;
		}
		
		// check if all segments + head fill the plane
		if (dimensions.x * dimensions.y == snake_segments.size() + 2) 
		{
			game_over_info = 1;
			return true;
		}
		

		// switch new snake-location from free to occupied
		for (int iter = 0; iter < free_spaces.size(); iter++)
			if (free_spaces.get(iter).equals(snake_location))
				free_spaces.remove(iter);
		snake_segments.addLast(new Point2D(snake_before_move)); // needs to be emplaced at the back
		// check if head is on apple
		if (snake_location.equals(apple_location))
		{
			// do not pop last segment and relocate apple
			RelocateApple(dimensions);
		}
		else
		{
			// pop last segment to move snake and add it to free_spaces
			free_spaces.add(new Point2D(snake_segments.getFirst()));
			snake_segments.removeFirst();
		}
		
		// check if player is touching one of the segments
		for (Point2D segment : snake_segments)
			if (segment.equals(snake_location)) 
			{
				return true;
			}
		
		
		return false;
	}
	/*
	 * Gibt das aktuelle Spielbrett aus
	 * #  - ist der Spielrand
	 * [] - ist ein Segment der Schlange
	 * () - ist der Kopf der Schlange
	 *  0 - ist der Apfel
	 */
	private static void Render(Point2D dimensions) 
	{
		
		// print top border
		System.out.print("#");
		for (int i = 0; i < dimensions.x; i++)
			System.out.print("##");
		System.out.print("#\n");
		
		// print board
		for (int i = 0; i < dimensions.y; i++) 
		{
			System.out.print("#");
			for (int j = 0; j < dimensions.x; j++) 
			{
				
				boolean issegment = false;
				for (Point2D segment : snake_segments)
					if (segment.equals(new Point2D(j,i)))
						issegment = true;
				
				if (new Point2D(j,i).equals(snake_location))
					System.out.print("()");
				else if (new Point2D(j,i).equals(apple_location))
					System.out.print(" 0");
				else if (issegment)
					System.out.print("[]");
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
	/*
	 * initialisiert und startet das Spiel
	 */
	private static void Mainloop(Scanner inputscanner, Point2D dimensions) 
	{
		
		for (int y = 0; y < dimensions.y; y++)
			for (int x = 0; x < dimensions.x; x++)
				free_spaces.add(new Point2D(x,y));
		
		// startfelder von snake (startet immer mit 2 Segmenten) und apfel sind immer gleich, diese müssen aus der liste der freien felder entfernt werden
		
		free_spaces.removeFirst();
		free_spaces.removeFirst();
		free_spaces.removeFirst();
		free_spaces.removeLast();
		
		snake_segments.add(new Point2D(0,0));
		snake_segments.add(new Point2D(1,0));
		
		apple_location = new Point2D(dimensions.x - 1, dimensions.y - 1);
		
		char input = 'd';
		
		boolean break_loop = false;
		
		Render(dimensions);
		while (!break_loop) 
		{
			input = GetInput(inputscanner);
			break_loop = UpdateGameState(input, dimensions);
			if (!break_loop)
				Render(dimensions);
		}
		if (game_over_info == 0) 
		{
			System.out.println("##############################");
			System.out.println("# GAME OVER - YOU LOST       #");
			System.out.println("##############################");
		}
		else if (game_over_info == 1) 
		{
			System.out.println("##############################");
			System.out.println("# GAME OVER - YOU WON        #");
			System.out.println("##############################");
		}
		else if (game_over_info == 2) 
		{
			System.out.println("##############################");
			System.out.println("# GAME OVER - QUIT BY PLAYER #");
			System.out.println("##############################");
		}
			
	}
}
