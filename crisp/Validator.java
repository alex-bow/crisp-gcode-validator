package crisp;

import java.util.List;
import java.util.ArrayList;

// Unlike the old implementation, this will wrap a LazyParser and use various
// modules to extract data from or otherwise make use of its tokens and data structures.
// It is the uppermost structure used here, the equivalent of the App or Game instance.
class Validator {
    LazyParser parser;
    private List<ValidationModule> modules;

    Validator(LazyParser parser) {
        this.parser = parser;
        this.modules = new ArrayList<ValidationModule>();
    }

    public void addModule(ValidationModule m) {
        this.modules.add(m);
    }

    public void validate() {
        for (ValidationModule m : modules) {
            m.validate(parser);
        }
    }
}
