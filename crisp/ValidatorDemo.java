package crisp;

import java.io.File;
import java.util.ArrayList;

class ValidatorDemo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File("./samples/MLib/CubeHighInfill.gcode"));
        Validator v = new Validator(p);
        v.addModule(new ConfigValidator());
        v.validate();
    }

}
