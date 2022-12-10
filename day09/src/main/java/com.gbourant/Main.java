package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        var moveLines = Files.readString(Path.of("moves.txt")).split(System.lineSeparator());

        part(moveLines, 1);
        part(moveLines, 9);
    }

    private static void part(String[] moveLines, int totalTails) {
        var head = new Position();
        var tails = new ArrayList<Position>();

        for (int i = 0; i < totalTails; i++) {
            tails.add(new Position());
        }

        Set<Position> totalTimesTailTouched = new HashSet<>();

        for (String moveLine : moveLines) {
            var splittedMove = moveLine.split("\\s");

            var direction = Direction.valueOf(splittedMove[0]);
            var distance = Integer.parseInt(splittedMove[1]);

            for (int i = 0; i < distance; i++) {

                switch (direction) {
                    case U -> head.y++;
                    case D -> head.y--;
                    case R -> head.x++;
                    case L -> head.x--;
                }

                tails.set(0, tails.get(0).move(head));

                for (int tailIndex = 1; tailIndex < totalTails; tailIndex++) {
                    tails.set(tailIndex, tails.get(tailIndex).move(tails.get(tailIndex - 1)));
                }

                totalTimesTailTouched.add(tails.get(totalTails - 1));
            }


        }

        System.out.println("total times touched " + totalTimesTailTouched.size());
    }

    static class Position {
        int x, y;


        public Position() {
        }

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (x != position.x) return false;
            return y == position.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        public Position move(Position head) {

            var diffX = Math.abs(x - head.x);
            var diffY = Math.abs(y - head.y);

            if (diffY >= 2 || diffX >= 2) {

                var p = new Position(head.x, head.y);

                if (diffY >= 2) {
                    p.y = y < head.y ? head.y - 1 : head.y + 1;
                }

                if (diffX >= 2) {
                    p.x = x < head.x ? head.x - 1 : head.x + 1;
                }

                return p;
            }


            return this;
        }
    }

    enum Direction {
        L, R, U, D
    }
}
