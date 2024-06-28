package crisp;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

class ConfigValidator extends ValidationModule {
    HashMap<String, List<String>> forbidden; // should be static

    ConfigValidator() {
        forbidden = new HashMap<String, List<String>>();
        forbidden.put("bridge_speed", List.of("30"));
    }

    void validate(LazyParser parser) {
        Class<? extends ConsumerModule> cl = new PrusaCommentConsumer(parser).getClass();
        ConsumerModule commentConsumer = parser.getConsumer(cl);
        if (commentConsumer == null) {
            System.err.println("No Prusa comment consumer present on parser.");
            return;
        }

        for (Map.Entry<String, List<String>> entry : forbidden.entrySet()) {
            Token value = commentConsumer.configs.get(entry.getKey());
            System.out.println(entry.getKey());
            System.out.println(value);
            if (value != null && (entry.getValue().contains(value.strValue) ||
                    entry.getValue().contains(value.value))) {
                System.err.println("Found error in GCODE on token " + entry.getKey() +
                    "; " + value.value + " or " + value.strValue + " is a forbidden value!");
            }
        }

    }
}
