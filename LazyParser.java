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
    ArrayList<TokenizerModule> tokenizerModules;

    private ArrayList<Token> tokens;
    private ArrayList<ParserStatus> parserStatuses;
    int pos;
    String currentLine;

    public int lineNum;
    private boolean isNewLine;

    LazyParser(File file) {
        this(file, new ArrayList<ParserModule>());
    }

    LazyParser(File file, ArrayList<ParserModule> pms) {
        lines = new ArrayList<Line>();
        tokens = new ArrayList<Token>();
        parserStatuses = new ArrayList<ParserStatus>(); // should be a set

        isNewLine = false;

        tokenizerModules = new ArrayList<TokenizerModule>();
        tokenizerModules.add(new PrinterGCodeTokenizer(this));
        tokenizerModules.add(new PrusaCommentTokenizer(this));

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

        for (lineNum = 1; scanner.hasNextLine(); lineNum++) {
            isNewLine = true;
            nextLineStr = scanner.nextLine().trim();
            if (!nextLineStr.isEmpty()) {
                tokenizeLine(nextLineStr, lineNum);
            }
        }
    }

    boolean isNewLine() {
        return isNewLine;
    }

    void clearNewLine() {
        isNewLine = false;
    }

    boolean hasStatus(ParserStatus status) {
        return parserStatuses.contains(status);
    }

    void clearStatus(ParserStatus status) {
        parserStatuses.remove(status);
    }

    void setStatus(ParserStatus status) {
        parserStatuses.add(status);
    }

    void tokenizeLine(String line, int ln) {
        currentLine = line;
        pos = 0;

        char c;
        TokenBase currentToken = null;
        int currentIdx = 0;
        double currentValue = 0.0;

        while (pos < line.length() - 1) {
            c = advance();
            for (TokenizerModule module : tokenizerModules) {
                if(module.caresAbout(c)) {
                    //System.out.println(line);
                    //System.out.println(module + " has taken an interest in " + c);
                    module.tokenize(c); 
                    // Tokenize will hold the loop until module creates a complete token
                }
            }
        }
    }

    // far - the # of chars to peek ahead. Will not peek past new line
    public char peek(int far) {
        return peek(currentLine, far);
    }

    private char peek(String line, int far) {
        if (pos + far < line.length()) {  
            return line.charAt(pos + far);
        } else {
            return '\0';
        }
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public Token lastToken() {
        if (tokens.size() > 0) {
            return tokens.get(tokens.size() - 1);
        } else {
            return null;
        }
        
    }

    public char advance() {
        return advance(currentLine);
    }

    private char advance(String line) {
        if (pos < line.length() - 1) {  
            return line.charAt(pos++);
        } else {
            return '\0';
        }
    }

    public void jump(int i) {
        pos += i;
    }
}