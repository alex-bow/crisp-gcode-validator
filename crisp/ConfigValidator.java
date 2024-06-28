package crisp;

class ConfigValidator extends ValidationModule {
    void validate(LazyParser parser) {
        Class<? extends ConsumerModule> cl = new PrusaCommentConsumer(parser).getClass();
        ConsumerModule commentConsumer = parser.getConsumer(cl);
        if (commentConsumer == null) {
            System.err.println("No Prusa comment consumer present on parser.");
            return;
        }
        System.out.println("Found Prusa comment consumer!");
    }
}
