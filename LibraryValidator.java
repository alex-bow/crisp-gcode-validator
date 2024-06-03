import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

class LibraryValidator extends Validator {
    Set<StatusType> validate(LazyParser parser) {
        System.out.println("About to validate " + parser.lines.size() + " lines");
        HashSet stata = new HashSet<StatusType>();
        Line l;
        boolean flagged = false;
        StatusType cmtStatus;
        for (int i = 0; i < parser.lines.size(); i++) {
            cmtStatus = null;
            l = parser.lines.get(i);
            if (l.comment) {
                if (l.params.size() > 1 && l.params.get(1).equals("=")) {
                    cmtStatus = check_prusa_comments(l.params);
                    if (cmtStatus != null) {
                        flagged = true;
                        stata.add(cmtStatus);
                    }
                }
            }
        }
        if (!flagged) {
            stata.add(StatusType.SUCCESS);
        }
        return stata;
    }

    StatusType check_prusa_comments(ArrayList<String> cmtWords) {
        if (cmtWords.size() < 3) {
            return null;
        } else if (cmtWords.get(0).equals("fill_density")) {
            int i;
            try {
                i = Integer.parseInt(cmtWords.get(2).replace("%",""));
            } catch (NumberFormatException e) {
                System.out.println(e);
                i = 0;
            }
            if (i > 60) {
                return StatusType.SOFT_FAIL_HIGH_INFILL;
            }
        } else if (cmtWords.get(0).equals("filament_type")) {
            if (!cmtWords.get(2).equals("PLA")) {
                return StatusType.HARD_FAIL_FILAMENT_TYPE;
            }
        }
        return null;
    }
}