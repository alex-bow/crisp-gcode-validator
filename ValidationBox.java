import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

class ValidationBox {
    LazyParser parser;
    ArrayList<Validator> validators;
    Set<StatusType> statuses; 

    ValidationBox(LazyParser newParser, ArrayList<Validator> newValidators) {
        parser = newParser;
        validators = newValidators; 
        statuses = new HashSet<StatusType>();
    }

    // can examine value of ValidationBox().statuses afterward
    void validate() {
        Set<StatusType> tempStatuses = new HashSet<StatusType>();
        System.out.println("About to validate " + parser.lines.size() + " lines");
        for (int i = 0; i < validators.size(); i++) {
            tempStatuses = validators.get(i).validate(parser);
            statuses.addAll(tempStatuses);
        }
    }

    void debug_display_statuses() {
        System.out.println(statuses);

        String msg = "";
        boolean failIsSoft;
        for (StatusType s : statuses) {
            if (s == StatusType.SOFT_FAIL_HIGH_INFILL || s == StatusType.SOFT_FAIL_HUGE_PRINT || s == StatusType.SOFT_FAIL_SUPPORT_TYPE) {
                failIsSoft = true;
            } else {
                failIsSoft = false;
            }
            // REPLACE - make modifiable (JSON?)
            switch (s) {
                case SUCCESS: msg = "No issues found in gcode!";
                case HARD_FAIL_PRINTER_TYPE: msg = "This was not sliced for a Prusa Mk3 printer.";
                case HARD_FAIL_PROFILE_VERSION: msg = "Your profiles are out of date; please download the latest profiles from the Library website.";
                case HARD_FAIL_FILAMENT_TYPE: msg = "This was sliced for a filament other than PLA.";
                case HARD_FAIL_NO_PROFILE: msg = "This was not sliced using our profiles; please download the latest profiles from the Library website.";
                case SOFT_FAIL_HIGH_INFILL: msg = "Your infill percentage is very high.";
                case SOFT_FAIL_SUPPORT_TYPE: msg = "You are using a non-recommended support type.";
                case SOFT_FAIL_HUGE_PRINT: msg = "Your print is very large (more than 6h or X grams of filament).";
            }
            if (failIsSoft) {
                msg = "Warning! " + msg + " This print has been designated for manual review.";
            }
            System.out.println(msg);
        }
    }
}