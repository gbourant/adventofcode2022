package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main
{
    public static void main( String[] args ) throws IOException {
        var rucksacksTxt = Files.readString(Path.of("rucksacks.txt"));
        var rucksacks = Arrays.asList(rucksacksTxt.split(System.lineSeparator()));

        var compartments = rucksacks.stream().map(r->
                List.of(r.substring(0,r.length()/2),r.substring(r.length()/2))
                ).toList();

        var sum = compartments.stream().map(Main::getSameChar).map(Main::getPriority).reduce(0,Integer::sum);
        System.out.println("sum = " + sum);

        var groupOfThree = IntStream.range(0,rucksacks.size())
                .boxed()
                .collect(Collectors.groupingBy(part->(part/3),Collectors.mapping(rucksacks::get,Collectors.toList()))).values();

        sum = groupOfThree.stream().map(Main::getSameChar).map(Main::getPriority).reduce(0,Integer::sum);
        System.out.println("sum = " + sum);
    }

    private static int getPriority(Character character) {
        return character - (Character.isUpperCase(character) ? 'A' -26 : 'a') +1;
    }

    private static Set<Character> getUnique(String str){
        return str.chars().mapToObj(c->(char) c).collect(Collectors.toSet());
    }

    private static char getSameChar(List<String> strings) {
        var unique = getUnique(strings.get(0));
        strings.stream().skip(1).forEach(s->unique.retainAll(getUnique(s)));
        return unique.iterator().next();
    }
}
