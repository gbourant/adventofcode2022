package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws IOException {

        var caloriesStr = Files.readString(Path.of("calories.txt"));

        var calories = Arrays.asList(caloriesStr.split(System.lineSeparator()));

        var caloriesSize = calories.size();
        var startIndex = 0;
        List<Elf> elves = new ArrayList<>();
        for (int index =0; index < caloriesSize; index ++ ){
            var calorie = calories.get(index);
            if("".equals(calorie) || index == caloriesSize-1){
                var elfCalories = calories.subList(startIndex,index).stream().map(Integer::parseInt).toList();
                var elf = new Elf(elfCalories);
                elves.add(elf);
                startIndex = index + 1;
            }
        }
        
        var elfWithMaxCalories = elves.stream().max(Comparator.comparing(Elf::totalCalories)).get();
        System.out.println("elfWithMaxCalories = " + elfWithMaxCalories + " total calories: " + elfWithMaxCalories.totalCalories());
        
        var sorted = elves.stream().map(Elf::totalCalories).sorted(Comparator.reverseOrder()).limit(3).toList();
        System.out.println("sorted = " + sorted);

        var totalOfThree = sorted.stream().reduce(0,Integer::sum);
        System.out.println("totalOfThree = " + totalOfThree);

    }

    record Elf(List<Integer> calories){
        public int totalCalories() {
            return calories.stream().reduce(0,Integer::sum);
        }
    }
}
