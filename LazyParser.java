import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// Transforms a GCode file into a structure of lines
class LazyParser {
    // ModularTokenizer
    Scanner scanner;
    ArrayList<Line> lines;
    ArrayList<ParserModule> parserModules;

    private ArrayList<Token> tokens;
    int pos;

    LazyParser(File file) {
        this(file, new ArrayList<ParserModule>());
    }

    LazyParser(File file, ArrayList<ParserModule> pms) {
        lines = new ArrayList<Line>();
        tokens = new ArrayList<Token>();
        pos = 0;
        parserModules = pms;

        try {
            scanner = new Scanner(file);
        } catch (IOException e) {
            System.out.println("Loading file failed");
            System.exit(1);
        }
        parseGcodeFile();
    }

    void handleLineError(int ln, String line, GCodeError error) {
        System.out.println("Error at line " + ln + ": " + error);
        System.out.println(line);
    }

    void parseGcodeFile() {
        // scanTokens
        String nextLineStr;

        for (int lineNum = 1; scanner.hasNextLine(); lineNum++) {
            nextLineStr = scanner.nextLine().trim();
            if (!nextLineStr.isEmpty()) {
                tokenizeLine(nextLineStr);
            }
        }
    }

    void tokenizeLine(String line) {
        pos = 0;
        char c;
        while (pos < line.length()) {
            c = advance(line);
        }
    }

    char advance(String line) {
        return line.charAt(pos++);
    }

    
}