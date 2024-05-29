package main;

import java.util.List;

public final class Main {
	public static void main(String[] args) {
		System.out.println();
		Regression result = Regression
					.newBuilder()
					.addCSVFile("C:/Users/roemer/Desktop/test.csv",
								"pres",
								"tsun")
					.build();
		
		System.out.println("k: " + result.getK());
		System.out.println("d: " + result.getD());
		java.util.Arrays.asList(result.getAllColumnNames()).forEach(System.out::println);
		
	}
	private static final List<Point> getListOfPoints() {
		return List.of(Point.valueOf(1, 3), Point.valueOf(4, 7));
	}
}

