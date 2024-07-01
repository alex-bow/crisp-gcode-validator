package crisp;

class Token {
    TokenBase type;
    int idx; // eg G00 - 0
    double value;
    String strValue;
    int line;

    Token(TokenBase type, int idx, double value, int line) {
        this.type = type;
        this.idx = idx;
        this.value = value;
        this.strValue = null;
        this.line = line;
    }

    Token(TokenBase type, String strValue, int line) {
        this.type = type;
        this.strValue = strValue;
        this.idx = 0;
        this.value = 0.0;
        this.line = line;
    }

    public String toString() {
        return "(" + type + " " + idx + " " + value + " " + strValue + " " + line + ")";
    }
}
