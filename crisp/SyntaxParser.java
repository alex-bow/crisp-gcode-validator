package crisp;

import java.util.List;

// LazyParser converts the file into tokens; this converts tokens into
// a syntax tree
class SyntaxParser {
    private final List<Token> tokens;
    private int current = 0;

    SyntaxParser(LazyParser lazy) {
        tokens = lazy.tokens;
    }

    Expr parse() {
        return expression();
    }

    private Expr expression() {
        return command();
    }

    private Expr command() {
        Token cmd = cmd();
        Expr param = null;

        // This currently grabs only one
        while (match(PrinterGCodeToken.PARAM)) {
            Token p = previous();
            param = new Expr.Param(p);
        }

        return new Expr.Command(cmd, param);
    }

    private Token cmd() {
        return null;
    }

    private boolean match(TokenBase... types) {
        for (TokenBase type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenBase type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == PrinterGCodeToken.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current -1);
    }
}
