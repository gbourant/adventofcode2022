package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Main {
    private static Long modulus;

    public static void main(String[] args) throws IOException {
        var monkeysTxt = Files.readString(Path.of("monkeys.txt")).split("\\s\\n");

        List<Monkey> monkeys = solution(monkeysTxt, 20);

        var res = monkeys.stream().map(m -> m.totalTimesChecked.get())
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .reduce(1L, Math::multiplyExact);

        System.out.println("res = " + res);

        monkeys = solution(monkeysTxt, 10_000);

        res = monkeys.stream().map(m -> m.totalTimesChecked.get())
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .reduce(1L, Math::multiplyExact);

        System.out.println("res = " + res);

    }

    private static List<Monkey> solution(String[] monkeysTxt, int totalRounds) {
        List<Monkey> monkeys = new ArrayList<>();

        for (String monkeyLine : monkeysTxt) {

            var monkeyLineSplitted = monkeyLine.split(System.lineSeparator());

            var operationLine = monkeyLineSplitted[2].split("Operation: new = old ")[1].split("\\s");
            var operationType = operationLine[0].equals("*") ? OperationType.MULTI : OperationType.ADD;
            var operationValue = operationLine[1].equals("old") ? 0 : Long.parseLong(operationLine[1]);

            var startingItems = Arrays.stream(monkeyLineSplitted[1].split("Starting items: ")[1].split(",")).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
            var operation = new Operation(operationValue, operationType);
            var divisibleBy = Long.parseLong(monkeyLineSplitted[3].split("\\s")[5]);
            var ifTrue = Integer.parseInt(monkeyLineSplitted[4].split("\\s")[9]);
            var ifFalse = Integer.parseInt(monkeyLineSplitted[5].split("\\s")[9]);

            monkeys.add(new Monkey(startingItems, operation, divisibleBy, ifTrue, ifFalse, new AtomicLong()));
        }

        monkeys.forEach(System.out::println);

        Main.modulus = monkeys.stream().map(Monkey::divisibleBy).reduce(1L, Math::multiplyExact);

        for (int round = 0; round < totalRounds; round++) {
            for (Monkey monkey : monkeys) {
                var actions = monkey.play(totalRounds);
                for (MonkeyAction action : actions) {
                    monkeys.get(action.monkeyTo).items.addAll(action.items);
                }
            }
        }
        return monkeys;
    }

    enum OperationType {
        ADD {
            long apply(long worry, long operationValue) {
                return Math.addExact(worry, operationValue);
            }
        }, MULTI {
            long apply(long worry, long operationValue) {
                return Math.multiplyExact(worry, operationValue == 0 ? worry : operationValue);
            }
        };

        abstract long apply(long worry, long operationValue);
    }

    record Operation(long operationValue, OperationType type) {

    }

    record MonkeyAction(List<Long> items, int monkeyTo) {
    }

    record Monkey(List<Long> items, Operation operation, long divisibleBy, int ifTrue, int ifFalse,
                  AtomicLong totalTimesChecked) {
        public List<MonkeyAction> play(int totalRounds) {

            var divItems = new ArrayList<Long>();
            var nonDivItems = new ArrayList<Long>();

            for (Long item : items) {

                item = operation.type.apply(item, operation.operationValue) / (totalRounds == 20 ? 3 : 1);

                var isDivisible = 0 == item % divisibleBy;
                if (totalRounds != 20) {
                    item = item % modulus;
                }
                if (isDivisible) {
                    divItems.add(item);
                } else {
                    nonDivItems.add(item);
                }

                totalTimesChecked.incrementAndGet();
            }

            items.clear();

            var actions = new ArrayList<MonkeyAction>();

            actions.add(new MonkeyAction(divItems, ifTrue));
            actions.add(new MonkeyAction(nonDivItems, ifFalse));

            return actions;
        }
    }
}
