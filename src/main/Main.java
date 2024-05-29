package main;

import java.util.List;

public final class Main {
	public static void main(String[] args) {
		
		Regression res = Regression.newBuilder()
					.addCSVFile("C:/Users/roemer/Desktop/test.csv",
								"tmax",
								"snow")
					.build();
		
		System.out.println(res);
		res.getAllPoints().stream().limit(15).forEach(System.out::println);
		System.out.println("...");
	}
	private static final List<Point> getListOfPoints() {
		return List.of(Point.valueOf(1, 3), Point.valueOf(4, 7));
	}
}

