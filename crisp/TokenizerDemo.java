package crisp;

import java.io.File;
import java.util.ArrayList;

class TokenizerDemo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File("./samples/MLib/CubeStandard.gcode"));
    }

}
