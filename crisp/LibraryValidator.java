package crisp;

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
                if (l.params.size() > 1) {
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
        String cmtName;
        String cmtParam;

        if (cmtWords.contains("=")) {
            String[] reconstructed = reconstructCmt(cmtWords);
            cmtName = reconstructed[0];
            cmtParam = reconstructed[1];

            // System.out.println("Attempting to parse " + cmtName + " = " + cmtParam);
            if (cmtName.equals("fill_density")) {
                int i;
                try {
                    i = Integer.parseInt(cmtParam.replace("%",""));
                } catch (NumberFormatException e) {
                    System.out.println(e);
                    i = 0;
                }
                if (i > 60) {
                    return StatusType.SOFT_FAIL_HIGH_INFILL;
                }
            } else if (cmtName.equals("filament_type")) {
                if (!cmtParam.equals("PLA")) {
                    return StatusType.HARD_FAIL_FILAMENT_TYPE;
                }
            } else if (cmtName.equals("printer_model")) {
                if (!cmtParam.equals("MK3S")) {
                    return StatusType.HARD_FAIL_PRINTER_TYPE;
                }
            } else if (cmtName.equals("printer_settings_id")) {
                if (!cmtParam.equals("Prusa i3 MK3S (Marriott Library)")) {
                    return StatusType.HARD_FAIL_NO_PROFILE;
                }
            } else if (cmtName.equals("estimated printing time (normal mode)")) {
                m = timeStamp.matcher(cmtParam);
                if (m.find()) {
                    hours = Integer.parseInt(m.group(1));
                    if (hours > 6) {
                        return StatusType.SOFT_FAIL_HUGE_PRINT;
                    }
                }

            }
        }

        return null;
    }

    // Slightly less horrible but very naive reconstruction
    String[] reconstructCmt(ArrayList<String> cmtWords) {
        boolean isAfterEquals = false;
        String cmtName = "";
        String cmtParam = "";

        for (int i = 0; i < cmtWords.size(); i ++) {
            if (cmtWords.get(i).equals("=")) {
                isAfterEquals = true;
            } else if (isAfterEquals) {
                cmtParam += cmtWords.get(i);
                if (i < cmtWords.size() - 1) {
                    cmtParam += " ";
                }
            } else {
                cmtName += cmtWords.get(i);
                if (i < cmtWords.indexOf("=") - 1) {
                    cmtName += " ";
                }
            }
        }
        return new String[]{cmtName, cmtParam};
    }
}
