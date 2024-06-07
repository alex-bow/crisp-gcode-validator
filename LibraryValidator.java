import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class LibraryValidator extends Validator {
    static Pattern timeStamp = Pattern.compile("(\\d+)h (\\d+)m (\\d+)s");

    Set<StatusType> validate(LazyParser parser) {
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
        Matcher m;
        int hours;

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
        } else if (cmtWords.get(0).equals("printer_model")) {
            if (!cmtWords.get(2).equals("MK3S")) {
                return StatusType.HARD_FAIL_PRINTER_TYPE;
            }
        } else if (cmtWords.get(0).equals("printer_settings_id")) {
            System.out.println(cmtWords);
            if (!cmtWords.get(2).equals("Prusa i3 MK3S (Marriott Library)")) {
                return StatusType.HARD_FAIL_NO_PROFILE;
            }
        } else if (cmtWords.get(0).equals("estimated printing time (normal mode)")) {
            System.out.println(cmtWords);
            m = timeStamp.matcher(cmtWords.get(2));
            if (m.find()) {
                hours = Integer.parseInt(m.group(0));
                System.out.println("I think this print will take " + hours + " from " + m);
                if (hours > 6) {
                    return StatusType.SOFT_FAIL_HUGE_PRINT;
                }
            }

        }
        return null;
    }
}