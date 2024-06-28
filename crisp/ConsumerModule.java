package crisp;

import java.util.List;
import java.util.HashMap;

abstract class ConsumerModule<T> {

    public T data; // Stores whatever data structure this type of module deals in

    List<Token> tokens;
    int current;

    ConsumerModule(LazyParser p) {
        this.tokens = p.tokens;
        current = 0;
    }

    void parseTokens() {
        while(!isAtEnd()) {
            examineToken(peek());
            advance();
        }
    }

    void examineToken(Token t) {

    }

    protected boolean match(TokenBase... types) {
        for (TokenBase type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    protected boolean check(TokenBase type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    protected Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    protected boolean isAtEnd() {
        return peek().type == PrinterGCodeToken.EOF;
    }

    protected Token peek() {
        return tokens.get(current);
    }

    protected Token previous() {
        return tokens.get(current -1);
    }
}
