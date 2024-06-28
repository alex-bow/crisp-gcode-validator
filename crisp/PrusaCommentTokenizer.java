package crisp;

class PrusaCommentTokenizer extends TokenizerModule {

    // For this module, refers to something that can start a relevant/considered comment
    static char[] relevantChars = {';'};

    PrusaCommentTokenizer(LazyParser parser) {
        super(parser);
    }

    boolean caresAbout(char c) {
        return (new String(relevantChars).contains("" + c) || parser.hasStatus(PrusaCommentTokenizerStatus.IN_KEY) ||
            parser.hasStatus(PrusaCommentTokenizerStatus.IN_VALUE));
    }

    void tokenize(char c) {
        if (new String(relevantChars).contains("" + c)) {
            parser.setStatus(PrusaCommentTokenizerStatus.IN_KEY);
        } else if (parser.hasStatus(PrusaCommentTokenizerStatus.IN_KEY)) {
            grabKey();
        } else if (parser.hasStatus(PrusaCommentTokenizerStatus.IN_VALUE)) {
            grabValue();
        }
    }

    void grabKey() {
        String keyStr = "";
        char next;
        int i;

        // Starts at 0 because count is incremented _after_ what's returned by advance()
        for (i = 0; true; i++) {
            next = parser.peek(i);
            if (next == '=') {
                Token key = new Token(PrusaCommentToken.KEY, keyStr.trim(), parser.lineNum);
                // System.out.println("Comment token " + key);
                parser.addToken(key);
                parser.clearStatus(PrusaCommentTokenizerStatus.IN_KEY);
                parser.setStatus(PrusaCommentTokenizerStatus.IN_VALUE);
                break;
            } else if (next == '\0') {
                // This was not a formatted comment, we don't care
                // FIX: SLOW
                parser.clearStatus(PrusaCommentTokenizerStatus.IN_KEY);
                break;
            } else {
                keyStr += next;
            }
        }
        parser.jump(i);
    }

    void grabValue() {
        String keyStr = "";
        char next;
        int i;

        // Starts at 0 because count is incremented _after_ what's returned by advance()
        for (i = 0; true; i++) {
            next = parser.peek(i);
            // Scopes to end of line (\0 indicates peek hit newline)
            if (next == '\0') {
                Token key = new Token(PrusaCommentToken.VALUE, keyStr.trim(), parser.lineNum);
                // System.out.println("Comment token " + key);
                parser.addToken(key);
                parser.clearStatus(PrusaCommentTokenizerStatus.IN_VALUE);
                break;
            } else {
                keyStr += next;
            }
        }
        parser.jump(i);
    }
}

enum PrusaCommentTokenizerStatus implements ParserStatus {
    IN_KEY,
    IN_VALUE
}
