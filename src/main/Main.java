package main;

import java.util.List;

public final class Main {
	public static void main(String[] args) {
		Regression res = Regression.ofCSVFile("C:/Users/roemer/Desktop/test.csv", "tavg", "snow");
		System.out.println(res);
	}
	private static final List<Point> getListOfPoints() {
		return List.of(Point.valueOf(1, 3), Point.valueOf(4, 7));
	}
}

