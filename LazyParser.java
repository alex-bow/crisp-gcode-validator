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
                tokenizeLine(nextLineStr, lineNum);
            }
        }
        System.out.println(tokens);
    }

    void tokenizeLine(String line, int ln) {
        pos = 0;
        char c;
        TokenBase currentToken = null;
        int currentIdx = 0;
        double currentValue = 0.0;

        while (pos < line.length()) {
            c = advance(line);
            if (c == 'G') {
                currentToken = PrinterGCodeToken.G_CMD;
            } else if (c == 'M') {
                currentToken = PrinterGCodeToken.M_CMD;
            } else if (c == 'X') {
                currentToken = PrinterGCodeToken.X_PM;
            } else if (c == 'Y') {
                currentToken = PrinterGCodeToken.Y_PM;
            } else if (c == 'Z') {
                currentToken = PrinterGCodeToken.Z_PM;
            } else if (c == 'I') {
                currentToken = PrinterGCodeToken.I_PM;
            } else if (c == 'J') {
                currentToken = PrinterGCodeToken.J_PM;
            } else if (c == 'R') {
                currentToken = PrinterGCodeToken.R_PM;
            } else if (c == ' ') {
                tokens.add(new Token(currentToken, currentIdx, currentValue, ln));

                currentToken = null;
                currentIdx = 0;
                currentValue = 0.0;
            }
        }
    }

    char advance(String line) {
        return line.charAt(pos++);
    }

    
}