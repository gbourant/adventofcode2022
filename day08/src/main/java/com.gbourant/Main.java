package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        var grid = Files.readString(Path.of("grid.txt")).split(System.lineSeparator());

        List<List<Integer>> trees = new ArrayList<>();

        for (String row : grid) {
            var column = row.split("");

            List<Integer> rowTrees = new ArrayList<>();

            for (String col : column) {
                rowTrees.add(Integer.parseInt(col));
            }

            trees.add(rowTrees);
        }

        System.out.println("trees = " + trees);

        var totalTreeRows = trees.size();
        var totalTreeColumns = trees.get(0).size();

        var directions = Direction.values();

        var totalVisibleTrees = 0;

        var best = 0;

        for (int x = 0; x < totalTreeRows; x++) {
            for (int y = 0; y < totalTreeColumns; y++) {

                boolean isVisibleTree = false;
                var score = 1;
                for (Direction direction : directions) {
                    int xx = x;
                    int yy = y;
                    boolean canView = true;
                    int distance = 1;
                    while (true) {
                        xx += direction.x;
                        yy += direction.y;

                        var isXInRange = ValueRange.of(0, totalTreeRows - 1).isValidValue(xx);
                        var isYInRange = ValueRange.of(0, totalTreeRows - 1).isValidValue(yy);

                        if (!(isXInRange && isYInRange)) {
                            distance--;
                            break;
                        }

                        if (trees.get(xx).get(yy) >= trees.get(x).get(y)) {
                            canView = false;
                            break;
                        }
                        distance++;
                    }
                    score *= distance;
                    if (canView) {
                        isVisibleTree = true;
                    }

                }
                best = Math.max(best, score);
                if (isVisibleTree) {
                    totalVisibleTrees++;
                }

            }
        }

        System.out.println("totalVisibleTrees = " + totalVisibleTrees);
        System.out.println("best = " + best);

    }

    enum Direction {
        NORTH(0, 1), SOUTH(0, -1), WEST(-1, 0), EAST(1, 0);

        private final int x, y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
