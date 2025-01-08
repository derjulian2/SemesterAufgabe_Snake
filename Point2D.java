package semester_aufgabe;

// Einfache Klasse zum Zusammenbündeln von x und y Werten
// Für Snake nur Integer-Werte benötigt
public class Point2D 
{
	
	public Point2D(Point2D vec) 
	{
		this.x = vec.x; this.y = vec.y;
	}
	
	public Point2D(int x, int y) 
	{
		this.x = x; this.y = y;
	}

	public boolean equals(Point2D other) 
	{
		if (this.x == other.x && this.y == other.y)
			return true;
		else
			return false;
	}
	
	public String toString() 
	{
		return "(" + x + ", " + y + ")";
	}
	
	public int x = 0;
	public int y = 0;
}
