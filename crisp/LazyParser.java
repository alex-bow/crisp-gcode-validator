package crisp;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

// Takes in a GCode file and creates a list of tokens defined by its TokenizerModules,
// then uses those tokens to create data structurse according to the grammars
// provided in its ConsumerModules.

// TokenizerModules may assign and read ParserStatuses for ease in parsing.

class LazyParser {

    Scanner scanner;

    List<TokenizerModule> tokenizerModules = new ArrayList<TokenizerModule>();
    List<ConsumerModule> consumerModules = new ArrayList<ConsumerModule>();

    List<Token> tokens = new ArrayList<Token>();
    private Set<ParserStatus> parserStatuses = new HashSet<ParserStatus>();
    int pos = 0;
    String currentLine;

    public int lineNum;
    private boolean isNewLine = false;

    LazyParser(File file) {

        tokenizerModules.add(new PrinterGCodeTokenizer(this));
        tokenizerModules.add(new PrusaCommentTokenizer(this));

        // consumerModules.add(new PrusaCommentConsumer(this));
        consumerModules.add(new ToolPathConsumer(this));

        try {
            scanner = new Scanner(file);
        } catch (IOException e) {
            System.out.println("Loading file failed");
            System.exit(1);
        }
        parseGcodeFile();

        // Each ConsumerModule can define its own grammar, up to
        // and including full ASTs
        for (ConsumerModule consum : consumerModules) {
            consum.parseTokens();
        }
    }

    ConsumerModule getConsumer(Class<? extends ConsumerModule> cType) {
        for (ConsumerModule c : consumerModules) {
            if (c.getClass() == cType) {
                return c;
            }
        }
        return null;
    }

    void handleLineError(int ln, String line, GCodeError error) {
        System.out.println("Error at line " + ln + ": " + error);
        System.out.println(line);
    }

    // Creates a list of tokens based on a GCode file
    void parseGcodeFile() {
        String nextLineStr;

        for (lineNum = 1; scanner.hasNextLine(); lineNum++) {
            isNewLine = true;
            nextLineStr = scanner.nextLine().trim();
            if (!nextLineStr.isEmpty()) {
                tokenizeLine(nextLineStr, lineNum);
            }
        }
        tokens.add(new Token(PrinterGCodeToken.EOF, 0, 0.0, lineNum));
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
