class PrinterGCodeTokenizer extends TokenizerModule {

    static char[] relevantChars = {'G', 'M', 'X', 'Y', 'Z', 'I', 'J', 'R'};
    int advancement;

    PrinterGCodeTokenizer(LazyParser parser) {
        super(parser);
    }

    boolean caresAbout(char c) {
        return new String(relevantChars).contains("" + c);
    }

    String grabDigits() {
        String valStr = "";
        char next;
        int i;

        // Starts at 0 because count is incremented _after_ what's returned by advance()
        for (i = 0; i <= LOOKAHEAD_MAX; i++) {
            next = parser.peek(i);
            if (isDigit(next)) {
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
                if (c == 'G') {
                    currentToken = PrinterGCodeToken.G_CMD;
                    digitGrab = grabDigits();
                    if (!digitGrab.equals("")) {
                        currentIdx = Integer.parseInt(digitGrab);
                    }
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