package crisp;

import java.util.List;
import java.util.ArrayList;
//
// import java.util.Set;
//
// // Base class for validation modules used by a ValidationBox; each module should validate on separate criteria
// abstract class Validator {
//     Set<StatusType> validate(LazyParser parser) {
//         return null;
//     }
// }

// Unlike the old implementation, this will wrap a LazyParser and use various
// modules to extract data from it; it is the uppermost structure used here, the
// equivalent of the App or Game instance.
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
