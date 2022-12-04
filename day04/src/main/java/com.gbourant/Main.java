package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Main
{
    public static void main( String[] args ) throws IOException {
        var pairsTxt = Files.readString(Path.of("pairs.txt"));

        var pairs = Arrays.stream(pairsTxt.split(System.lineSeparator())).map(Main::toPair).toList();

        var pairsWithOverlap = pairs.stream().filter(Pair::hasOverlap).toList();
        System.out.println("pairsWithOverlap = " + pairsWithOverlap);
        System.out.println("pairsWithOverlap = " + pairsWithOverlap.size());

        var pairsWithSomeOverlap = pairs.stream().filter(Pair::hasSomeOverlap).toList();
        System.out.println("pairsWithSomeOverlap = " + pairsWithSomeOverlap);
        System.out.println("pairsWithSomeOverlap = " + pairsWithSomeOverlap.size());


    }

    private static Pair toPair(String pair) {
        var pairs = pair.split(",");
        return new Pair(new Range(pairs[0].split("-")),new Range(pairs[1].split("-")));
    }

    record Range(int start,int end){
        Range(String range[]){
            this(Integer.parseInt(range[0]),Integer.parseInt(range[1]));
        }
    }
    record Pair(Range firstRange,Range secondRange){
        public boolean hasOverlap() {
            return (firstRange.start <= secondRange.start && secondRange.end <= firstRange.end) ||
                    (secondRange.start <= firstRange.start && firstRange.end <= secondRange.end);
        }

        public boolean hasSomeOverlap() {
            return !(firstRange.start > secondRange.end || firstRange.end < secondRange.start);
        }
    }
}
