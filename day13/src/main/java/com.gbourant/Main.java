package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {

        var packets = Files.readAllLines(Path.of("packets.txt")).stream().filter(Predicate.not(String::isEmpty)).collect(Collectors.toList());

        var groupedPackets = IntStream.range(0, packets.size())
                .boxed()
                .collect(Collectors.groupingBy(partition -> (partition / 2), Collectors.mapping(packets::get, Collectors.toList())))
                .values()
                .stream().map(s -> List.of(Main.toPacket(s.get(0)), Main.toPacket(s.get(1))))
                .collect(Collectors.toList());

        var res = 0;

        for (int index = 0; index < groupedPackets.size(); index++) {
            var p1 = groupedPackets.get(index).get(0);
            var p2 = groupedPackets.get(index).get(1);

            if (compare(p1, p2) == -1) {
                res += index + 1;
            }
        }

        System.out.println("res = " + res);

        var p2 = List.of(2);
        var p6 = List.of(6);

        groupedPackets.add(List.of(p2, p6));

        var allPackets = groupedPackets.stream().flatMap(List::stream)
                .sorted(Main::compare)
                .collect(Collectors.toList());

        res = (allPackets.indexOf(p2) + 1) * (allPackets.indexOf(p6) + 1);
        System.out.println("res = " + res);


    }

    private static int compare(Object left, Object right) {

        if (left instanceof Integer i1 && right instanceof Integer i2) {
            return Integer.compare(i1, i2);
        } else if (!(left instanceof Integer) && !(right instanceof Integer)) {

            var ll = (List<Object>) left;
            var rr = (List<Object>) right;

            for (int i = 0; i < Math.min(ll.size(), rr.size()); i++) {
                int result = compare(ll.get(i), rr.get(i));
                if (result != 0) {
                    return result;
                }
            }
            return Integer.compare(ll.size(), rr.size());
        } else if (left instanceof Integer) {
            return compare(List.of(left), right);
        }

        return compare(left, List.of(right));

    }

    private static Object toPacket(String packetLine) {

        Stack<List<Object>> packets = new Stack<>();
        packets.add(new ArrayList<>());

        StringBuilder number = new StringBuilder();

        for (char c : packetLine.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            } else if (c == '[') {
                List<Object> packetList = new ArrayList<>();
                packets.peek().add(packetList);
                packets.push(packetList);
            } else if (c == ']' || c == ',') {
                if (!number.isEmpty()) {
                    packets.peek().add(Integer.parseInt(number.toString()));
                    number = new StringBuilder();
                }
                if (c == ']') {
                    packets.pop();
                }
            }
        }

        return packets.pop();
    }
}
