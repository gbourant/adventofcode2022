package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        var stream = Files.readString(Path.of("stream.txt"));

        var streamCharArray = stream.toCharArray();

        var signal = new ArrayList<String>();

        finStartOf(streamCharArray, signal, 4);
        finStartOf(streamCharArray, signal, 14);
    }

    private static void finStartOf(char[] streamCharArray, ArrayList<String> signal, int messageSize) {
        for (int i = 0; i < streamCharArray.length; i++) {
            var letter = Character.toString(streamCharArray[i]);
            signal.add(letter);

            var hasDistinct = signal.stream().distinct().count() == signal.size();

            if (signal.size() == messageSize && hasDistinct) {
                System.out.println("i = " + (i + 1));
                break;
            }

            if (signal.size() == messageSize) {
                signal.remove(0);
            }
        }
    }
}
