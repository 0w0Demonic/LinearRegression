package main;

import java.util.List;

public final class Main {
	public static void main(String[] args) {
		
		Regression result = Regression
					.newBuilder()
					.addCSVFile("C:/Users/roemer/Desktop/test.csv",
								"tmax",
								"snow")
					.build();
		
		System.out.println(result);
		result.getAllPoints().stream().limit(10).forEach(System.out::println);
		
	}
	private static final List<Point> getListOfPoints() {
		return List.of(Point.valueOf(1, 3), Point.valueOf(4, 7));
	}
}

