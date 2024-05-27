package main;



/**
 * An immutable Tuple of two double values.
 */
public final class Point implements Comparable<Point> {
	/**
	 * The field x
	 */
	private final double x;
	
	/**
	 * The field y
	 */
	private final double y;
	
	/**
	 * Returns the x-value of this Point.
	 * @return     the x-value of this instance.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the y-value of this Point.
	 * @return     the y-value of this instance.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Creates a new Point instance.
	 * @param x    the x-value
	 * @param y    the y-value
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Static factory method of Point.
	 * @param x    the x-value
	 * @param y    the y-value
	 * @return     a new Point instance
	 */
	public static final Point valueOf(double x, double y) {
		return new Point(x, y);
	}
	
	/**
	 * Returns a String representation of this Point.
	 * @return     a String representation in the format of "Point [123.00, 354.21]" 
	 */
	@Override
	public String toString() {
		return new StringBuilder(128)
			.append("Point [")
			.append(this.x)
			.append(", ")
			.append(this.y)
			.append("]")
			.toString();
	}
	
	/**
	 * Checks for equality with another Object.
	 * @return     a boolean
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Point other
				&& other.x == this.x
				&& other.y == this.y;
	}
	
	/**
	 * Returns the hash code of this Point.
	 * @return     an int
	 */
	@Override
	public int hashCode() {
		return (31 * (int)this.x) +
					(int)this.y;
	}
	
	/**
	 * Lexicographically compares this Point with another Point as
	 * implementation of the Comparable<Point> interface.
	 * @return     an int
	 */
	public int compareTo(Point other) {
		int result = Double.compare(this.x, other.x);
		if (result != 0)
			return result;
		return Double.compare(this.y, other.y);
	}
}
