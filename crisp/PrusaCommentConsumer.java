package crisp;

import java.util.List;
import java.util.ArrayList;

class PrusaCommentConsumer extends ConsumerModule {
    static int MAX_PARAMS = 999; // Probably not ideal implementation

    List<PrusaCommentConfig> configs;

    PrusaCommentConsumer(LazyParser p) {
        super(p);
        configs = new ArrayList<PrusaCommentConfig>();
    }

    void examineToken(Token t) {
        if (t.type instanceof PrusaCommentToken) {
            // we don't care about any other types

            if (check(PrusaCommentToken.KEY)) {
                advance();
                if (check(PrusaCommentToken.VALUE))
                    System.out.println("About to add config " + t + " " + peek());
                    configs.add(new PrusaCommentConfig(t, peek()));
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
