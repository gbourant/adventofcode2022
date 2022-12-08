package com.gbourant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        var terminalLines = Files.readString(Path.of("terminal.txt")).split(System.lineSeparator());

        var fileSystem = new FileSystem(null, new ArrayList<>(), "/", 0, FileType.DIRECTORY);

        FileSystem currentDirectory = null;

        for (String terminalLine : terminalLines) {

            var splittedLine = terminalLine.split("\\s");

            var firstParam = splittedLine[0];
            var secondParam = splittedLine[1];

            var isCommand = firstParam.startsWith("$");

            var isFile = firstParam.matches("^\\d.+");

            if (isCommand) {

                if (terminalLine.startsWith("$ ls")) {
                    continue;
                }

                if ("..".equals(splittedLine[2])) {
                    currentDirectory = currentDirectory.parent;
                } else {
                    var fileName = splittedLine[2];
                    currentDirectory = getDirectory(fileSystem, currentDirectory, fileName);
                }

            } else if (isFile) {
                var newFile = new FileSystem(currentDirectory, new ArrayList<>(), secondParam, Integer.parseInt(firstParam), FileType.FILE);
                currentDirectory.child.add(newFile);
            } else {
                //directory
                var newFile = new FileSystem(currentDirectory, new ArrayList<>(), secondParam, 0, FileType.DIRECTORY);
                currentDirectory.child.add(newFile);
            }

        }

        var totalSize = new ArrayList<Integer>();

        var finalTotalSize = calculateSize(fileSystem, fileSystem.size, totalSize);

        System.out.println("finalTotalSize = " + finalTotalSize);
        System.out.println("totalSize = " + totalSize);

        var res = totalSize.stream().filter(size -> size <= 100_000).reduce(0, Integer::sum);
        System.out.println("res = " + res);

        var fileSystemSize = 70_000_000;
        var needSpace = 30_000_000;

        var unusedScape = fileSystemSize - finalTotalSize;
        var lowestNeededSpace = needSpace - unusedScape;

        System.out.println("lowestNeededSpace = " + lowestNeededSpace);

        res = totalSize.stream().sorted().filter(size -> size >= lowestNeededSpace).toList().get(0);
        System.out.println("res = " + res);

    }

    private static int calculateSize(FileSystem fileSystem, int fileSize, List<Integer> totalSize) {
        var finalSize = 0;

        if (fileSystem.type == FileType.DIRECTORY) {
            totalSize.add(fileSystem.getSize());
        }

        for (FileSystem system : fileSystem.child) {
            finalSize += calculateSize(system, system.size, totalSize);
        }


        return finalSize + fileSize;
    }

    private static FileSystem getDirectory(FileSystem fileSystem, FileSystem currentDirectory, String fileName) {
        if (currentDirectory == null) {
            return fileSystem;
        }

        return currentDirectory.child.stream().filter(f -> f.name.equals(fileName)).toList().get(0);
    }

    enum FileType {
        DIRECTORY, FILE
    }

    record FileSystem(FileSystem parent, List<FileSystem> child, String name, int size, FileType type) {

        public int getSize() {
            return this.child.stream().map(FileSystem::getSize).reduce(0, Integer::sum) + this.size;
        }
    }
}
