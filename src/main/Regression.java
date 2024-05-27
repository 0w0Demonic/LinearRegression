package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class consisting of the fields r and d, and a Builder class.
 * Example:
 * 
 * List<Point> pointList = getPoints();
 * Result myResult = Result.newBuilder().add(Point.valueOf(23, 45))
 * 							.add(0.33, 0.982).add(pointList).build();
 * Result myResult2 = Result.ofPointList(getPoints());
 */
public final class Regression {
	/**
	 * The field r
	 */
	final private double r;
	
	/**
	 * The field d
	 */
	final private double d;
	
	/**
	 * List of all points of this linear regression
	 */
	final private List<Point> points;
	
	/**
	 * @return r            the value r
	 */
	public double getR() {
		return this.r;
	}
	
	/**
	 * @return d           	the value d
	 */
	public double getD() {
		return this.d;
	}
	
	/**
	 * @return points		the List<Points> of all points in this linear regression
	 */
	public List<Point> getAllPoints() {
		return this.points;
	}
	
	/**
	 * @param list          an instance of Regression.Builder
	 * @returns             an immutable Result instance
	 */
	private Regression(Builder builder) {
		// early initialization convinces the VM to be less trash lmfao
		if (builder.list.size() < 2)
			throw new IllegalArgumentException(
					"Not enough points given to make an appropriate linear regression;"
					+ "At least 2 points are needed.");
		this.points = builder.list;
		Point p = null;
		int size = this.points.size();
		double sumX = 0, sumY = 0, sumXY = 0,
				meanX, meanY, tempX, tempY,
				sx, sy, sxy, r, k, d;
		
		// get means
		for (int i = 0; i < size; i++) {
			p = this.points.get(i);
			sumX += p.getX();
			sumY += p.getY();
		}
		meanX = sumX / size;
		meanY = sumY / size;
		
		// get sx, sy, sxy
		sumX = 0;
		sumY = 0;
 		for (int i = 0; i < size; i++) {
			p = this.points.get(i);
			tempX = (p.getX() - meanX);
			tempY = (p.getY() - meanY);
			sumX += tempX * tempX;
			sumY += tempY * tempY;
			sumXY += tempX * tempY;
		}
		sx = Math.sqrt(sumX / (size - 1));
		sy = Math.sqrt(sumY / (size - 1));
		sxy = sumXY / (size - 1);
		
		// get r, k, d
		r = sxy / (sx * sy);
		k = r * sy / sx;
		d = meanY - (k * meanX);
		this.r = r;
		this.d = d;
	}
	
	/**
	 * String representation of the Regression instance.
	 * @returns            a String in the format of "Result [r = 123, d = 321]"
	 */
	@Override
	public String toString() {
		return new StringBuilder(128)
			.append("Result [r = ")
			.append(this.r)
			.append(", d = ")
			.append(this.d)
			.append("]")
			.toString();
	}
	
	/**
	 * @param c            a Collection<Point> instance
	 * @return             an immutable Result instance
	 */
	public static final Regression ofPoints(Collection<Point> c) {
		return newBuilder().addPoints(c).build();
	}
	
	/**
	 * @param points       an array of points
	 * @return             an immutable Result instance
	 */
	public static final Regression ofPoints(Point... arr) {
		return newBuilder().addPoints(arr).build();
	}
	
	/**
	 * @param doubleList   a list of doubles (alternating x and y values)
	 * @return             an immutable Result instance
	 */
	public static final Regression ofDoubles(Collection<Double> c) {
		return newBuilder().addDoubles(c).build();
	}
	
	/**
	 * @param doubleArray  an array of doubles (alternating x and y values)
	 * @return             an immutable Result instance
	 */
	public static final Regression ofDoubles(double... arr) {
		return newBuilder().addDoubles(arr).build();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// A massive amount of static factory methods
	public static final Regression ofCSVFile(String filePath) {
		return newBuilder().addCSVFile(Path.of(filePath), 0, 1).build();
	}
	public static final Regression ofCSVFile(String filePath, int i1, int i2) {
		return newBuilder().addCSVFile(Path.of(filePath), i1, i2).build();
	}
	public static final Regression ofCSVFile(String filePath, String name1, String name2) {
		return newBuilder().addCSVFile(Path.of(filePath), name1, name2).build();
	}
	public static final Regression ofCSVFile(File file) {
		return newBuilder().addCSVFile(file.toPath(), 0, 1).build();
	}
	public static final Regression ofCSVFile(File file, int i1, int i2) {
		return newBuilder().addCSVFile(file.toPath(), i1, i2).build();
	}
	public static final Regression ofCSVFile(File file, String name1, String name2) {
		return newBuilder().addCSVFile(file.toPath(), name1, name2).build();
	}
	public static final Regression ofCSVFile(Path path) {
		return newBuilder().addCSVFile(path, 0, 1).build();
	}
	public static final Regression ofCSVFile(Path path, int i1, int i2) {
		return newBuilder().addCSVFile(path, i1, i2).build();
	}
	public static final Regression ofCSVFile(Path path, String name1, String name2) {
		return newBuilder().addCSVFile(path, name1, name2).build();
	}
	/////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an instance of Regression.Builder.
	 * @return             a new Regression.Builder instance
	 */
	public static final Builder newBuilder() {
		return new Regression.Builder();
	}
	
	/**
	 * The class that is used to build an instance of Regression.
	 */
	public static final class Builder {
		/**
		 * Temporary storage that hold a list of Point instances
		 */
		private final List<Point> list = new ArrayList<>();
		
		/**
		 * Private constructor.
		 * Accessible via the Result.newBuilder() method.
		 */
		private Builder() {}
		
		/**
		 * Adds a Point instance to the Builder.
		 * @param p             the Point instance
		 * @return              the Builder itself
		 */
		public Builder add(Point p) {
			this.list.add(p);
			return this;
		}
		
		/**
		 * Adds a Point instance to the Builder.
		 * @param x             the x value of the new Point
		 * @param y             the y value of the new Point
		 * @return              the Builder itself
		 */
		public Builder add(double x, double y) {
			this.list.add(Point.valueOf(x, y));
			return this;
		}
		
		/**
		 * Adds Point instances to the Builder.
		 * @param c             a Collection<Point> instance
		 * @return              the Builder itself
		 */
		public Builder addPoints(Collection<Point> c) {
			this.list.addAll(c);
			return this;
		}
		
		/**
		 * Adds Point instances to the Builder.
		 * @param points        an array of points
		 * @return              the Builder itself
		 */
		public Builder addPoints(Point... points) {
			for (int i = 0; i < points.length; i++) {
				this.list.add(points[i]);
			}
			return this;
		}
		
		/**
		 * Adds Point instances to the Builder.
		 * @param c             a Collection<Double> instance
		 * @return              the Builder itself
		 */
		public Builder addDoubles(Collection<Double> c) {
			int estimateSize = (int)c.spliterator().getExactSizeIfKnown();
			int counter = 0;
			
			if (c instanceof ArrayList<Double> al) {
				for (int i = 0, n = checkArgumentLength(estimateSize); i < n; )
					this.list.add(Point.valueOf(al.get(i++),
												al.get(i++)));
				return this;
			}
			
			for (var iterator = c.iterator(); iterator.hasNext(); ++counter)
				this.list.add(Point.valueOf(iterator.next(),
											iterator.next()));
			checkArgumentLength(counter);
			return this;
		}
		
		/**
		 * Adds Point instances to the Builder.
		 * @param doubles       an array of doubles
		 * @return              the Builder itself
		 */
		public Builder addDoubles(double... doubles) {
			int length = checkArgumentLength(doubles.length);
			for (int i = 0; i < length; ++i) {
				this.list.add(Point.valueOf(doubles[i],
											doubles[++i]));
			}
			return this;
		}
		
		
		public Builder addCSVFile(String filePath) {
			return this.addCSVFile(Path.of(filePath), 0, 1);
		}
		public Builder addCSVFile(String filePath, int i1, int i2) {
			return this.addCSVFile(Path.of(filePath), i1, i2);
		}
		public Builder addCSVFile(String filePath, String name1, String name2) {
			return this.addCSVFile(Path.of(filePath), name1, name2);
		}
		public Builder addCSVFile(File file) {
			return this.addCSVFile(file.toPath(), 0, 1);
		}
		public Builder addCSVFile(File file, int i1, int i2) {
			return this.addCSVFile(file.toPath(), i1, i2);
		}
		public Builder addCSVFile(File file, String name1, String name2) {
			return this.addCSVFile(file.toPath(), name1, name2);
		}
		public Builder addCSVFile(Path path) {
			return this.addCSVFile(path, 0, 1);
		}
		public Builder addCSVFile(Path path, int i1, int i2) {
			if (!Files.exists(path))
				throw new IllegalArgumentException("Specified path does not exist");
			
			if (i1 == i2)
				throw new IllegalArgumentException("i1 = " + i1 + ", i2 = " + i2);
			
			try (BufferedReader br = Files.newBufferedReader(path)) {
				String line = br.readLine(); // Header
				while ((line = br.readLine()) != null) {
					String[] elements = line.split(",");
					String elementX = elements[i1];
					String elementY = elements[i2];
					double xValue = Double.parseDouble(elementX);
					double yValue = Double.parseDouble(elementY);
					this.list.add(Point.valueOf(xValue, yValue));
				}
			} catch (IOException e) {
				System.err.println("The CSV-file could not be read from.");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return this;
		}
		public Builder addCSVFile(Path path, String name1, String name2) {
			if (name1 == null || name1.isBlank()
					|| name2 == null || name2.isBlank()
					|| name1.equals(name2))
				throw new IllegalArgumentException("Invalid column names");
			
			try (BufferedReader br = Files.newBufferedReader(path)) {
				String[] elements = br.readLine().split(",");
				int i1 = -1;
				int i2 = -1;
				for (int i = 0; i < elements.length; i++) {
					String element = elements[i];
					if (element.equals(name1)) i1 = i;
					if (element.equals(name2)) i2 = i;
				}
				
				if (i1 == -1)
					throw new IllegalArgumentException("Column #1 not found");
				
				if (i2 == -1)
					throw new IllegalArgumentException("Column #2 not found");
				
				if (i1 == i2)
					throw new AssertionError("Both columns are of the same index, "
								+ "this should not happen.");
				
				return this.addCSVFile(path, i1, i2);
			} catch (IOException e) {
				System.err.println("The CSV-File could not be found or read from.");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return this;
		}
		
		/**
		 * Ensures that the number of arguments is even.
		 * @param  length       the length of the data container
		 * @return              the length of the data container
		 * @throws IllegalArgumentException when length is an
		 *         odd number. (Points consisting of
		 *         each an x- and a y-value)
		 */
		private static final int checkArgumentLength(int length) {
			if (length % 2 != 0)
				throw new IllegalArgumentException(
						"An odd number of parameters was passed");
			return length;
		}
		
		/**
		 * Builds a new Result instance
		 * @return              an immutable Result instance
		 */
		public Regression build() {
			return new Regression(this);
		}
	}
}
