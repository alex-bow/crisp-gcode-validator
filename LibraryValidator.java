import java.util.Set;
import java.util.HashSet;

class LibraryValidator extends Validator {
    Set<StatusType> validate(LazyParser parser) {
        HashSet stata = new HashSet<StatusType>();
        Line l;
        for (int i = 0; i < parser.lines.size(); i++) {
            l = parser.lines.get(i);
            stata.add(StatusType.SUCCESS);
        }
        return stata;
    }
}