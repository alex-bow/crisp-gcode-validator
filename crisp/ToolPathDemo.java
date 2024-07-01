package crisp;

import java.io.File;
import java.util.ArrayList;

class ToolPathDemo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File("./samples/MLib/CubeHighInfill.gcode"));
    }

}
