package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        var planTxt = Files.readString(Path.of("plan.txt"));

        var plan = planTxt.split("\\s\\d[\\d\\s]+\\d");

        var totalStacks = Main.getTotalStacks(planTxt);

        var stacksInArray = Arrays.asList(plan[0].split(System.lineSeparator()));
        var stacks = Main.createStacks(stacksInArray, totalStacks);
        System.out.println("stacks = " + stacks);

        var moves = Arrays.asList(plan[1].replaceAll("(?m)^\\n", "").split(System.lineSeparator()));
        System.out.println("moves = " + moves);

        moveBoxes(stacks, moves, false);
        System.out.println("result = " + stacks.stream().map(Stack::peek).collect(Collectors.joining()));

        stacks = Main.createStacks(stacksInArray, totalStacks);
        moveBoxes(stacks, moves, true);
        System.out.println("result = " + stacks.stream().map(Stack::peek).collect(Collectors.joining()));

    }

    private static void moveBoxes(List<Stack<String>> stacks, List<String> moves, boolean keepOrder) {
        for (String move : moves) {
            var splittedMoves = move.split("\\s");

            var totalBoxesToMove = Integer.parseInt(splittedMoves[1]);
            var fromStack = Integer.parseInt(splittedMoves[3]);
            var toStack = Integer.parseInt(splittedMoves[5]);

            List<String> boxesToMove = new ArrayList<>();

            for (int i = 0; i < totalBoxesToMove; i++) {
                boxesToMove.add(stacks.get(fromStack - 1).pop());
            }

            if (keepOrder) {
                for (int i = boxesToMove.size(); i > 0; i--) {
                    stacks.get(toStack - 1).push(boxesToMove.get(i - 1));
                }
            } else {
                boxesToMove.forEach(stacks.get(toStack - 1)::push);
            }

        }
    }

    private static int getTotalStacks(String planTxt) {
        var patternNumber = Pattern.compile("\\s\\d[\\d\\s]+\\d");
        var matcherNumber = patternNumber.matcher(planTxt);
        matcherNumber.find();
        return matcherNumber.group().split("\\s\\d").length;

    }

    private static List<Stack<String>> createStacks(List<String> stacks, int totalStacks) {
        return IntStream.range(0, totalStacks).boxed().map(index -> Main.createStack(index, stacks)).toList();
    }

    private static Stack<String> createStack(Integer stackIndex, List<String> stacks) {
        var stack = new Stack<String>();

        for (int i = stacks.size(); i > 0; i--) {
            try {
                var space = stackIndex + 1;
                var letterIndex = space + stackIndex * 3;
                var letter = Character.toString(stacks.get(i - 1).charAt(letterIndex));
                if (!" ".equals(letter))
                    stack.push(letter);
            } catch (StringIndexOutOfBoundsException ignored) {

            }
        }
        return stack;
    }
}
