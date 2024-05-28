package main;

import java.util.List;

public final class Main {
	public static void main(String[] args) {
		Point p = new Point(1.23, 345.9); // alternativ auch Point.valueOf(1.23, 245.9);
		double x = p.getX();
		double y = p.getY();
		
		// String, File oder java.nio.file.Path
		// Optional: zwei Strings (Namen der Spalten), oder zwei ints (0-based index)
		Regression res = Regression.ofCSVFile("C:/Users/roemer/Desktop/test.csv",
											  "tavg",
											  "snow");
		
		List<Point> points = res.getAllPoints();
		points.forEach(System.out::println);
		
		double r = res.getR();
		double d = res.getD();
		String nameOfXColumn = res.getColumnNameX();
		String nameOfYColumn = res.getColumnNameY();
		String[] columns = res.getAllColumnNames();
		
		
		res = Regression.newBuilder()
					.add(12.4, 2134.0)
					.add(Point.valueOf(234, 9))
					.addDoubles(23.34, 23.67, 785.435, 543.2)
					.addCSVFile("C:/Users/roemer/Desktop/test.csv",
								"tavg",
								"snow")
					.build();
		
		System.out.println(res);
		
		
	}
	private static final List<Point> getListOfPoints() {
		return List.of(Point.valueOf(1, 3), Point.valueOf(4, 7));
	}
}

