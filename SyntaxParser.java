import java.util.List;

import static TokenType.*;

// LazyParser converts the file into tokens; this converts tokens into
// a syntax tree
class SyntaxParser {
    private final List<Token> tokens;
    private int current = 0;

    SyntaxParser(LazyParser lazy) {
        tokens = lazy.tokens;
    }

    private Expr expression() {
        return instruction();
    }

    private Expr instruction() {
        Expr expr = command();

        while (match(PARAM)) {
            Token param = previous();
            expr = new Expr.Param(param);
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) [
            if (check(type)) {
                advance();
                return true;
            }
        ]
        return false;
    }
}
