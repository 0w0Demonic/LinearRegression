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
	/////////////////////////////////////////////////////////////////////////////
	// private final fields, and their public getter methods.
	private final double k;
	private final double d;
	private final double[] xValues;
	private final double[] yValues;
	private final List<Point> points;
	private final String columnNameX;
	private final String columnNameY;
	private final String[] columnNames;
	
	public double getK() {
		return this.k;
	}
	public double getD() {
		return this.d;
	}
	public double[] getAllXValues() {
		return this.xValues;
	}
	public double[] getAllYValues() {
		return this.yValues;
	}
	public List<Point> getAllPoints() {
		return this.points;
	}
	public String getColumnNameX() {
		return this.columnNameX;
	}
	public String getColumnNameY() {
		return this.columnNameY;
	}
	public String[] getAllColumnNames() {
		return this.columnNames;
	}
	/////////////////////////////////////////////////////////////////////////////
	// Constructor which is invoked by using Regression.Builder.build();
	// This is where the result is going to be calculated.
	private Regression(Builder builder) {
		ensure(builder.list.size() >= 3, "Not enough points were provided to make an appropriate "
				+ "linear regression graph. At least 3 points are needed");
		
		this.points = builder.list;
		this.columnNameX = builder.columnNameX;
		this.columnNameY = builder.columnNameY;
		this.columnNames = builder.columnNames;
		
		
		final int size = this.points.size();
		this.xValues = new double[size];
		this.yValues = new double[size];
		Point p = null;
		boolean allPointsSameXValue = true;
		double sumX = 0, sumY = 0, sumXY = 0,
				x, y,
				firstXValue = this.points.get(0).getX(),
				meanX, meanY,
				deltaX, deltaY,
				sx, sy, sxy,
				r, k, d;
		
		// get means
		for (int i = 0; i < size; i++) {
			p = this.points.get(i);
			x = p.getX();
			y = p.getY();
			sumX += (this.xValues[i] = x);
			sumY += (this.yValues[i] = y);
			if (x != firstXValue)
				allPointsSameXValue = false;
		}
		
		ensure(!allPointsSameXValue, "All provided points have the same x value.");
		
		meanX = sumX / size;
		meanY = sumY / size;
		
		// get sx, sy, sxy
		sumX = 0;
		sumY = 0;
		
 		for (int i = 0; i < size; i++) {
 			deltaX = this.xValues[i] - meanX;
 			deltaY = this.yValues[i] - meanY;
			sumX += deltaX * deltaX;
			sumY += deltaY * deltaY;
			sumXY += deltaX * deltaY;
		}
 		
 		sx = Math.sqrt(sumX / (size - 1));
		sy = Math.sqrt(sumY / (size - 1));
		sxy = sumXY / (size - 1);
		
		// get r, k, d
		r = sxy / (sx * sy);
		r = Double.isFinite(r) ? r : 0;
		
		k = r * sy / sx;
		System.out.println(k);
		this.k = Double.isFinite(k) ? k : 0;
		
		d = meanY - (k * meanX);
		System.out.println(d);
		this.d = Double.isFinite(d) ? d : 0;
	}
	
	// String representation in the format of "Result [r = 123, d = 321]"
	@Override
	public String toString() {
		return new StringBuilder(128)
			.append("Result [k = ")
			.append(this.k)
			.append(", d = ")
			.append(this.d)
			.append("]")
			.toString();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// Simple static factories
	//
	// Supported params:
	// 1. double...
	// 2. Point...
	// 3. Collection<Double>
	// 4. Collection<Point>
	
	public static final Regression ofPoints(Collection<Point> c) {
		return newBuilder().addPoints(c).build();
	}
	
	public static final Regression ofPoints(Point... arr) {
		return newBuilder().addPoints(arr).build();
	}
	
	public static final Regression ofDoubles(Collection<Double> c) {
		return newBuilder().addDoubles(c).build();
	}
	
	public static final Regression ofDoubles(double... arr) {
		return newBuilder().addDoubles(arr).build();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// CSV file static factories
	//
	// Supported params:
	// 1. String|File|Path (file)
	// 2. String|File|Path (file), int   (column index), int   (column index)
	// 3. String|File|Path (file), String (column name), String (column name)
	//
	// function signature #1 will assume x and y to be the first two columns.
	
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
	// Returns a new instance of Regression.Builder.
	public static final Builder newBuilder() {
		return new Regression.Builder();
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// Class that constructs new Regression instances
	public static final class Builder {
		// ArrayList<Point> of all points
		private final List<Point> list = new ArrayList<>();
		
		// Names of columns read in a CSV file, default to "x", "y".
		private String columnNameX = "x";
		private String columnNameY = "y";
		private String[] columnNames = {"x", "y"};
		
		// private constructor
		// Use Regression.newBuilder() to get a new instance
		private Builder() {}
		
		// Initializes calculation
		public Regression build() {
			return new Regression(this);
		}
		
		/////////////////////////////////////////////////////////////////////////
		// Simple add methods
		//
		// Point
		// Point[]
		// double, double
		// double[]
		// Collection<Point>
		// Collection<Double>
		
		public Builder add(Point p) {
			this.list.add(p);
			return this;
		}
		public Builder add(double x, double y) {
			this.list.add(Point.valueOf(x, y));
			return this;
		}
		public Builder addPoints(Collection<Point> c) {
			this.list.addAll(c);
			return this;
		}
		public Builder addPoints(Point... points) {
			for (int i = 0; i < points.length; i++) {
				this.list.add(points[i]);
			}
			return this;
		}
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
		public Builder addDoubles(double... doubles) {
			int length = checkArgumentLength(doubles.length);
			for (int i = 0; i < length; ++i) {
				this.list.add(Point.valueOf(doubles[i],
											doubles[++i]));
			}
			return this;
		}
		
		/////////////////////////////////////////////////////////////////////////
		// add methods for CSV files
		//
		// supported params:
		// 1. String|File|Path (file)
		// 2. String|File|Path (file), int   (column index), int   (column index)
		// 3. String|File|Path (file), String (column name), String (column name)
		//
		// function signature #1 will assume x and y to be the first two columns.
		// 
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
			ensure(Files.exists(path), "Specified path does not exist: " + path.toString());
			ensure(i1 != i2, "Not allowed to use the same column for both x and y values: " + i1);
			ensure(i1 >= 0, "Index of x values is not allowed to be negative: " + i1);
			ensure(i2 >= 0, "Index of y values is not allowed to be negative: " + i2);
			
			String line;
			String[] elements;
			try (BufferedReader br = Files.newBufferedReader(path))
			{
				line = br.readLine();
				elements = line.split(",");
				ensure(elements.length >= i1,
						"Argument index1 is outside of valid bounds 0 and " + (elements.length-1));
				
				ensure(elements.length >= i2,
						"Argument index2 is outside of valid bounds 0 and " + (elements.length-1));
				
				this.columnNames = elements;
				this.columnNameX = elements[i1];
				this.columnNameY = elements[i2];
				
				while ((line = br.readLine()) != null) {
					elements = line.split(",");
					String elementX = elements[i1];
					String elementY = elements[i2];
					double xValue = Double.parseDouble(elementX);
					double yValue = Double.parseDouble(elementY);
					this.list.add(Point.valueOf(xValue, yValue));
				}
			}
			catch (IOException e)
			{
				System.err.println("The CSV file could not be read from.");
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
			return this;
		}
		public Builder addCSVFile(Path path, String name1, String name2) {
			ensure(name1 != null && !name1.isBlank(), "Name 1 is null or empty.");
			ensure(name2 != null && !name2.isBlank(), "Name 2 is null or empty.");
			ensure(!name1.equals(name2), "Not allowed to use the same column for both x and x values.");
			ensure(Files.exists(path), "Unable to find specified CSV file.");
			
			try (BufferedReader br = Files.newBufferedReader(path)) {
				String[] elements = br.readLine().split(",");
				ensure(elements.length >= 2, "The specified CSV file must have at least 2 columns.");
				int i1 = -1;
				int i2 = -1;
				for (int i = 0; i < elements.length; i++) {
					String element = elements[i];
					if (element.equals(name1)) i1 = i;
					if (element.equals(name2)) i2 = i;
				}
				
				ensure(i1 != -1, "Column #1 not found.");
				ensure(i2 != -1, "Column #2 not found.");
				ensure(i1 != i2, "Both columns have the same index. This should normally not happen.");
				
				return this.addCSVFile(path, i1, i2);
			} catch (IOException e) {
				System.err.println("The CSV file could not read from.");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return this;
		}
		
		// validates argument length
		// must be even and > 0
		private static final int checkArgumentLength(int length) {
			ensure(length % 2 == 0, "An odd number of parameters was passed.");
			ensure(length != 0, "No arguments provided.");
			return length;
		}
		
	}
	// Assertions and error checking
	private static final void ensure(boolean assertion, String msg) {
		if (!assertion) throw new IllegalArgumentException(msg);
	}
}
