class PrinterGCodeTokenizer extends TokenizerModule {

    static char[] relevantChars = {'G', 'M', 'X', 'Y', 'Z', 'I', 'J', 'R'};
    int advancement;
    private boolean justIgnored; // Prevents IGNORE tokens from stacking up

    PrinterGCodeTokenizer(LazyParser parser) {
        super(parser);
    }

    boolean caresAbout(char c) {
        if (parser.hasStatus(PrinterGCodeStatus.COMMENT)) {
            if (parser.isNewLine()) {
                parser.clearNewLine();
                parser.clearStatus(PrinterGCodeStatus.COMMENT);
            } else {
                return false;
            }
        }
        return new String(relevantChars).contains("" + c);
    }

    String grabDigits(boolean decimal) {
        String valStr = "";
        char next;
        int i;

        // Starts at 0 because count is incremented _after_ what's returned by advance()
        for (i = 0; i <= LOOKAHEAD_MAX; i++) {
            next = parser.peek(i);
            if (isDigit(next) || (decimal && next == '.' && i > 0)) {
                valStr += next;
            } else {
                break;
            }
        }
        parser.jump(i);
        return valStr;
    }

    // from craftinginterpreters
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    } 

    void tokenize(char c) {
        TokenBase currentToken = null;
        int currentIdx = 0;
        double currentValue = 0.0;
        boolean done = false;
        String digitGrab;

        while (!done) {
            if (currentToken == null) {
                if (c == 'G' && parser.startingLine()) {
                    currentToken = PrinterGCodeToken.G_CMD;
                    digitGrab = grabDigits(false);
                    if (!digitGrab.isEmpty()) {
                        currentIdx = Integer.parseInt(digitGrab);
                    }
                } else if (c == 'M' && parser.startingLine()) {
                    currentToken = PrinterGCodeToken.M_CMD;
                    digitGrab = grabDigits(false);
                    if (!digitGrab.isEmpty()) {
                        currentIdx = Integer.parseInt(digitGrab);
                    }
                } else if (c == 'X') {
                    currentToken = PrinterGCodeToken.X_PM;
                    digitGrab = grabDigits(true);
                    if (!digitGrab.isEmpty()) {
                        currentValue = Double.parseDouble(digitGrab);
                    }
                    justIgnored = false;
                } else if (c == 'Y') {
                    currentToken = PrinterGCodeToken.Y_PM;
                    digitGrab = grabDigits(true);
                    if (!digitGrab.isEmpty()) {
                        currentValue = Double.parseDouble(digitGrab);
                    }
                    justIgnored = false;
                } else if (c == 'Z') {
                    currentToken = PrinterGCodeToken.Z_PM;
                    digitGrab = grabDigits(true);
                    if (!digitGrab.isEmpty()) {
                        currentValue = Double.parseDouble(digitGrab);
                    }
                    justIgnored = false;
                } else if (c == 'I') {
                    currentToken = PrinterGCodeToken.I_PM;
                    justIgnored = false;
                } else if (c == 'J') {
                    currentToken = PrinterGCodeToken.J_PM;
                    justIgnored = false;
                } else if (c == 'R') {
                    currentToken = PrinterGCodeToken.R_PM;
                    justIgnored = false;
                } else {
                    if (!justIgnored) {
                        Token t = new Token(PrinterGCodeToken.IGNORE, 0, 0.0, parser.lineNum);
                        System.out.println("Ignoring...");
                        parser.addToken(t);
                        done = true;
                        justIgnored = true;
                        return;
                    }
                }
            } else {
                if (c == ' ') {
                    Token t = new Token(currentToken, currentIdx, currentValue, parser.lineNum);
                    System.out.println("Adding " + t);
                    parser.addToken(t);
                    done = true;
                    return;
                } 
            }
            c = parser.advance();
            if (c == '\0') {
                done = true;
                return;
            }
        }        
    }
}