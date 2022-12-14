package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        var mapTxt = Files.readAllLines(Path.of("map.txt"));

        solution(mapTxt,1);
        solution(mapTxt,2);

    }

    private static void solution(List<String> mapTxt, int part) {
        List<List<Integer>> map = new ArrayList<>();

        var steps = new ArrayDeque<Point>();
        Point endpoint = null;
        for (int rowIndex = 0; rowIndex < mapTxt.size(); rowIndex++) {
            var row = new ArrayList<Integer>();
            var line = mapTxt.get(rowIndex).toCharArray();
            for (int columnIndex = 0; columnIndex < line.length; columnIndex++) {
                var currentChar = line[columnIndex];
                var charAsInt = Character.getNumericValue(currentChar) - Character.getNumericValue('a') + 1;

                if (currentChar == 'S' || (part == 2 && currentChar == 'a')) {
                    row.add(1);
                    steps.add(new Point(rowIndex, columnIndex,0));
                } else if (currentChar == 'E') {
                    endpoint = new Point(rowIndex, columnIndex,0);
                    row.add(26);
                } else {
                    row.add(charAsInt);
                }
            }

            map.add(row);
        }


        var totalRows = map.size();
        var totalColumns = map.get(0).size();

        var directions = Direction.values();

        Set<Point> visited = new HashSet<>();

        while (!steps.isEmpty()) {
            var step = steps.removeFirst();

            if (visited.contains(step)) {
                continue;
            }
            visited.add(step);
            if (step.equals(endpoint)) {
                endpoint = step;
                break;
            }

            for (Direction direction : directions) {
                var rr = step.row + direction.x;
                var cc = step.column + direction.y;


                if (cc >= 0 && cc < totalColumns && rr >= 0 && rr < totalRows && map.get(rr).get(cc) <= map.get(step.row).get(step.column) + 1) {
                    steps.add(new Point(rr, cc,step.totalSteps + 1));
                }

            }
        }

        System.out.println("endpoint.totalSteps = " + endpoint.totalSteps);
    }

    enum Direction {
        WEST(-1, 0),
        EAST(1, 0),
        SOUTH(0, -1),
        NORTH(0, 1);

        private final int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    record Point(int row, int column,int totalSteps) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (row != point.row) return false;
            return column == point.column;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + column;
            return result;
        }
    }

}
