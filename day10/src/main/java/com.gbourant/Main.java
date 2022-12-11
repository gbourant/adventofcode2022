package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        var ops = Files.readAllLines(Path.of("ops.txt"));

        var cycle = 0;
        var registerX = 1;
        var signal = 0;

        var cyclesToCheck = List.of(20, 60, 100, 140, 180, 220);

        List<List<Character>> crt = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < 6; rowIndex++) {
            List<Character> row = new ArrayList<>();
            for (int pixelIndex = 0; pixelIndex < 40; pixelIndex++) {
                row.add('.');
            }
            crt.add(row);
        }


        for (String op : ops) {
            cycle++;
            if ("noop".equals(op)) {
                signal = calculateSignal(cycle, registerX, signal, cyclesToCheck, crt);
            } else {
                signal = calculateSignal(cycle, registerX, signal, cyclesToCheck, crt);
                cycle++;
                signal = calculateSignal(cycle, registerX, signal, cyclesToCheck, crt);
                var value = Integer.parseInt(op.split("\\s")[1]);
                registerX += value;

            }

        }

        System.out.println(cycle);
        System.out.println("registerX = " + registerX);
        System.out.println("signal = " + signal);
        crt.forEach(System.out::println);

    }

    private static int calculateSignal(int cycle, int registerX, int signal, List<Integer> cyclesToCheck, List<List<Character>> crt) {

        var row = (cycle - 1) / 40;
        System.out.println("row = " + row);
        var pos = (cycle - 1) % 40;
        System.out.println("pos = " + pos);

        if(Math.abs(registerX - pos) <= 1){
            crt.get(row).set(pos,'#');
        }

        if (cyclesToCheck.contains(cycle)) {
            return signal + (cycle * registerX);
        }

        return signal;
    }
}
