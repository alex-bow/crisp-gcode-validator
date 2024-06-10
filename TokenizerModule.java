class TokenizerModule {
    LazyParser parser;

    protected static int LOOKAHEAD_MAX = 2;

    // Perhaps this is not an appropriate use of public
    TokenizerModule(LazyParser parser) {
        this.parser = parser;
    }

    boolean caresAbout(char c) {
        return false;
    }

    void tokenize(char c) {
    }
}