package semester_aufgabe;

// two dimensional vector for bundling together two floats
public class vec2f {
	
	public vec2f(vec2f vec) {
		this.x = vec.x; this.y = vec.y;
	}
	
	public vec2f(float x, float y) {
		this.x = x; this.y = y;
	}

	public boolean equals(vec2f other) {
		if (this.x == other.x && this.y == other.y)
			return true;
		else
			return false;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public float x = 0;
	public float y = 0;
}
