class Token {
    TokenBase type;
    int idx; // eg G00 - 0
    double value;
    int line;

    Token(TokenBase type, int idx, double value, int line) {
        this.type = type;
        this.idx = idx;
        this.value = value; 
        this.line = line;
    }
}