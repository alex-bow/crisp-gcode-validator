// usage after compiling: java Demo path/to/file.gcode

import java.io.File;
import java.util.ArrayList;

class Demo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File(args[0]));
        ArrayList<Validator> v = new ArrayList<Validator>();
        v.add(new LibraryValidator());
        ValidationBox vb = new ValidationBox(p, v);
        vb.validate();
        vb.debug_display_statuses();
    }
}