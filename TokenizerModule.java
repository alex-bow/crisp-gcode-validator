class TokenizerModule {
    LazyParser parser;

    // This is not great, might slow performance, but handles decimals
    protected static int LOOKAHEAD_MAX = 8; 

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