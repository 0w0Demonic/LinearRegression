package main;

import java.util.List;

public final class Main {
	public static void main(String[] args) {
		
//		Regression res = Regression.newBuilder()
//					.addCSVFile("C:/Users/roemer/Desktop/test.csv",
//								"tmax",
//								"snow")
//					.build();
		Regression res = Regression.ofDoubles(1, 0, 1, 1, 0.9, 2);
		System.out.println(res);
	}
	private static final List<Point> getListOfPoints() {
		return List.of(Point.valueOf(1, 3), Point.valueOf(4, 7));
	}
}

