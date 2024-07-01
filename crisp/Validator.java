package crisp;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

// Unlike the old implementation, this will wrap a LazyParser and use various
// modules to extract data from or otherwise make use of its tokens and data structures.
// It is the uppermost structure used here, the equivalent of the App or Game instance.
class Validator {
    LazyParser parser;
    private List<ValidationModule> modules = new ArrayList<ValidationModule>();
    private Set<StatusType> statuses = new HashSet<StatusType>();

    Validator(LazyParser parser) {
        this.parser = parser;
    }

    public void addModule(ValidationModule m) {
        this.modules.add(m);
    }

    public void validate() {
        for (ValidationModule m : modules) {
            m.validate(parser, this);
        }
        report_errors();
    }

    public void send_status(StatusType s) {
        statuses.add(s);
    }

    public void report_errors() {
        for (StatusType s : statuses) {
            String msg = "";
            // REPLACE - make modifiable (JSON?)
            switch (s) {
                case StatusType.SUCCESS:
                    msg = "No issues found in gcode!";
                    break;
                case StatusType.HARD_FAIL_PRINTER_TYPE:
                    msg = "This was not sliced for a Prusa Mk3 printer.";
                    break;
                case StatusType.HARD_FAIL_PROFILE_VERSION:
                    msg = "Your profiles are out of date; please download the latest profiles from the Library website.";
                    break;
                case StatusType.HARD_FAIL_FILAMENT_TYPE:
                    msg = "This was sliced for a filament other than PLA.";
                    break;
                case StatusType.HARD_FAIL_NO_PROFILE:
                    msg = "This was not sliced using our profiles; please download the latest profiles from the Library website.";
                    break;
                case StatusType.SOFT_FAIL_HIGH_INFILL:
                    msg = "Your infill percentage is very high.";
                    break;
                case StatusType.SOFT_FAIL_SUPPORT_TYPE:
                    msg = "You are using a non-recommended support type.";
                    break;
                case StatusType.SOFT_FAIL_HUGE_PRINT:
                    msg = "Your print is very large (more than 6h or X grams of filament).";
                    break;
            }
            System.err.println(msg);
        }
    }
}
