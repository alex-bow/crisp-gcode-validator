package crisp;

class PrinterGCodeTokenizer extends TokenizerModule {

    static char[] relevantChars = {'G', 'M', 'X', 'Y', 'Z', 'I', 'J', 'R', ';'};
    int advancement;

    PrinterGCodeTokenizer(LazyParser parser) {
        super(parser);
    }

    boolean caresAbout(char c) {
        if (parser.hasStatus(PrinterGCodeStatus.COMMENT)) {
            if (parser.isNewLine()) {
                parser.clearStatus(PrinterGCodeStatus.COMMENT);
            } else {
                return false;
            }
        }
        return true; // new String(relevantChars).contains("" + c);
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
                // System.out.print(c);
                // Restructure all
                if (c == 'G' && parser.isNewLine()) {
                    currentToken = PrinterGCodeToken.G_CMD;
                    digitGrab = grabDigits(false);
                    if (!digitGrab.isEmpty()) {
                        currentIdx = Integer.parseInt(digitGrab);
                    }

                } else if (c == 'M' && parser.isNewLine()) {
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

                } else if (c == 'Y') {
                    currentToken = PrinterGCodeToken.Y_PM;
                    digitGrab = grabDigits(true);
                    if (!digitGrab.isEmpty()) {
                        currentValue = Double.parseDouble(digitGrab);
                    }

                } else if (c == 'Z') {
                    currentToken = PrinterGCodeToken.Z_PM;
                    digitGrab = grabDigits(true);
                    if (!digitGrab.isEmpty()) {
                        currentValue = Double.parseDouble(digitGrab);
                    }

                } else if (c == 'I') {
                    // I and J are currently nonfunctional (It will never register the token because it is looking for
                    // I rather than I00, etc
                    currentToken = PrinterGCodeToken.I_PM;
                } else if (c == 'J') {
                    currentToken = PrinterGCodeToken.J_PM;
                } else if (c == 'R') {
                    currentToken = PrinterGCodeToken.R_PM;
                    digitGrab = grabDigits(false);
                    if (!digitGrab.isEmpty()) {
                        currentValue = Integer.parseInt(digitGrab);
                    }
                } else if (c == ';') {
                    // Comments can start midline
                    // TODO: does gcode escape comments?
                    // System.out.println("Comment beginning on line " + parser.lineNum);
                    parser.setStatus(PrinterGCodeStatus.COMMENT);
                    done = true;
                } else {
                    if (parser.lastToken().type != PrinterGCodeToken.IGNORE) {
                        currentToken = PrinterGCodeToken.IGNORE;
                    }
                }
            } else {
                if (c == ' ') {
                    Token t = new Token(currentToken, currentIdx, currentValue, parser.lineNum);
                    // System.out.println("Adding " + t);
                    parser.addToken(t);
                    done = true;
                }
            }

            // FIX: Some commands don't set done? Or why does this prevent infinite loop
            if (!done) {
                c = parser.advance();
                if (c == '\0') {
                    // Hack to make sure tokens at EOL are added
                    if (currentToken != null) {
                        Token t = new Token(currentToken, currentIdx, currentValue, parser.lineNum);
                        // System.out.println("Adding " + t);
                        parser.addToken(t);
                    }
                    done = true;
                }
            }
            if (parser.isNewLine()) {
                parser.clearNewLine();
            }
        }
    }
}
