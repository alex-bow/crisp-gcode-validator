package crisp;

// usage after compiling: java Demo path/to/file.gcode

import java.io.File;
import java.util.ArrayList;

class ValidatorDemo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File("./samples/MLib/CubeStandard.gcode"));
        Validator v = new Validator(p);
        v.addModule(new ConfigValidator());
        v.validate();
    }

}
