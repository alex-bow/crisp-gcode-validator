package crisp;

import java.util.HashMap;

class PrusaCommentConsumer extends ConsumerModule {
    HashMap<Token, Token> configs;

    PrusaCommentConsumer(LazyParser p) {
        super(p);
        configs = new HashMap<Token, Token>();
        // The data structure a ConsumerModule uses can be this simple for this
        // simple of a grammar
    }

    void examineToken(Token t) {
        if (t.type instanceof PrusaCommentToken) {
            // we don't care about any other types

            if (check(PrusaCommentToken.KEY)) {
                advance();
                if (check(PrusaCommentToken.VALUE))
                    System.out.println("About to add config " + t + " " + peek());
                    configs.put(t, peek());
                }
            }

            // // Should replace this with match() per book
            // if (t.type == PrusaCommentToken.KEY) {
            //     Token next = peek(1);
            //     if (next.type == PrusaCommentToken.VALUE) {
            //         advance();
            //         configs.add(new PrusaCommentConfig(t, next));
            //     }
            // }
    }

}
