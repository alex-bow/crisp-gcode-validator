import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class LazyParser {
    Scanner scanner;
    ArrayList<Line> lines;

    LazyParser(File file) {
        try {
            scanner = new Scanner(file);
        } catch (IOException e) {
            System.out.println("Loading file failed");
            // exit
        }
        parseGcodeFile();
    }

    void parseGcodeFile() {
        String nextLineStr;
        String[] words;
        boolean lineIsComment;
        Line nextLine;
        GCodeCommand cmd;
        int start;
        while (scanner.hasNextLine()) {
            nextLineStr = scanner.nextLine();
            if (!nextLineStr.trim().isEmpty()) {
                lineIsComment = (nextLineStr.substring(0,1).equals(";"));
                words = nextLineStr.split("[\\s]");
                if (lineIsComment) {
                    cmd = null;
                    start = 0;
                } else {
                    cmd = new GCodeCommand(words[0]);
                    start = 1;
                }
                nextLine = new Line(lineIsComment, cmd, 
                new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(words, start, words.length))));
            }
        }
    }
}