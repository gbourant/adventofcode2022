package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        var gamesTxt = Files.readString(Path.of("games.txt"));
        var splittedGames = gamesTxt.split(System.lineSeparator());
        var games = Arrays.stream(splittedGames).map(g -> g.split("\s")).toList();

        List<Integer> score = new ArrayList<>();

        for (String[] game : games) {
            var firstPlayerShape = Shape.map(ShapeStr.valueOf(game[0]));
            var secondPlayerShape = Shape.map(ShapeStr.valueOf(game[1]));
            var gameResult = new Game(firstPlayerShape, secondPlayerShape).play();
            score.add(gameResult);
        }

        var finalScore = score.stream().reduce(0, Integer::sum);
        System.out.println("finalScore = " + finalScore);

        score.clear();

        for (String[] game : games) {
            var firstPlayerShape = Shape.map(ShapeStr.valueOf(game[0]));
            var secondPlayerShape = firstPlayerShape.getShape(GameResult.map(GameResultStr.valueOf(game[1])));
            var gameResult = new Game(firstPlayerShape, secondPlayerShape).play();
            score.add(gameResult);
        }

        finalScore = score.stream().reduce(0, Integer::sum);
        System.out.println("finalScore = " + finalScore);
    }

    enum GameResultStr {
        X, Y, Z
    }

    enum GameResult {
        WIN, LOSE, DRAW;

        static GameResult map(GameResultStr gameResultStr) {
            return switch (gameResultStr) {
                case X -> LOSE;
                case Y -> DRAW;
                case Z -> WIN;
            };
        }
    }

    record Game(Shape firstPlayerShape, Shape secondPlayerShape) {
        public int play() {
            var shapePoint = secondPlayerShape.getShapePoint();
            var gameResult = secondPlayerShape.play(firstPlayerShape);
            return shapePoint + gameResult;
        }
    }

    enum ShapeStr {
        A, B, C, X, Y, Z
    }

    enum Shape {
        ROCK, PAPER, SCISSORS;

        public int getShapePoint() {
            return this.ordinal() + 1;
        }

        static Shape map(ShapeStr shapeStr) {
            return switch (shapeStr) {
                case A, X -> ROCK;
                case B, Y -> PAPER;
                case C, Z -> SCISSORS;
            };
        }

        public Shape getBeatShape() {
            return switch (this) {
                case PAPER -> ROCK;
                case ROCK -> SCISSORS;
                case SCISSORS -> PAPER;
            };
        }

        public Shape getShape(GameResult gameResult) {
            return switch (gameResult) {
                case WIN -> this.getLoseShape();
                case DRAW -> this;
                case LOSE -> this.getBeatShape();
            };
        }

        private Shape getLoseShape() {
            return switch (this) {
                case PAPER -> SCISSORS;
                case ROCK -> PAPER;
                case SCISSORS -> ROCK;
            };
        }

        public int play(Shape shape) {
            if (this == shape) {
                return 3;
            }

            if (this.getBeatShape() == shape) {
                return 6;
            }

            return 0;

        }
    }
}
