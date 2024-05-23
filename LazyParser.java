import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
        while (scanner.hasNextLine()) {
            nextLineStr = scanner.nextLine();
            System.out.println(nextLineStr);
        }
    }
}