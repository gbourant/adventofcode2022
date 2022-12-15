package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        var paths = Files.readAllLines(Path.of("path.txt"));
        solution(paths, 1);
        solution(paths, 2);
    }

    private static void solution(List<String> paths, int part) {
        var points = new HashSet<Point>();

        for (String pathLine : paths) {
            var splittedLine = pathLine.split(" -> ");

            Point prevPoint = null;
            for (String path : splittedLine) {
                var coordinates = path.split(",");
                var x = Integer.parseInt(coordinates[0]);
                var y = Integer.parseInt(coordinates[1]);

                if (prevPoint != null) {
                    int dx = x - prevPoint.x;
                    int dy = y - prevPoint.y;

                    var distance = Math.max(Math.abs(dx), Math.abs(dy)) + 1;

                    for (int i = 0; i < distance; i++) {
                        int directionX = Integer.compare(dx, 0);
                        int directionY = Integer.compare(dy, 0);
                        int xx = prevPoint.x + i * directionX;
                        int yy = prevPoint.y + i * directionY;
                        points.add(new Point(xx, yy));
                    }
                }

                prevPoint = new Point(x, y);

            }

        }

        int res = 0;

        var startingPoint = new Point(500, 0);

        var maxY = points.stream().mapToInt(Point::y).max().getAsInt() + (part == 2 ? 2 : 0);

        if (part == 2) {
            IntStream.range(-10_000, 10_000).forEach(x -> points.add(new Point(x, maxY)));
        }

        while (simulate(points, startingPoint, maxY)) {
            res++;
        }

        System.out.println("res = " + (res + part - 1));
    }

    private static boolean simulate(HashSet<Point> points, Point startingPoint, int maxY) {

        if (startingPoint.y > maxY) {
            return false;
        }

        var testPoint = new Point(startingPoint.x, startingPoint.y + 1);

        if (!points.contains(testPoint)) {
            return simulate(points, testPoint, maxY);
        }

        testPoint = new Point(startingPoint.x - 1, startingPoint.y + 1);

        if (!points.contains(testPoint)) {
            return simulate(points, testPoint, maxY);
        }

        testPoint = new Point(startingPoint.x + 1, startingPoint.y + 1);

        if (!points.contains(testPoint)) {
            return simulate(points, testPoint, maxY);
        }
        points.add(startingPoint);


        return !(startingPoint.x == 500 && startingPoint.y == 0);
    }

    record Point(int x, int y) {
    }
}