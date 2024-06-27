package crisp;

import java.io.File;
import java.util.ArrayList;

class ParserDemo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File("./samples/MLib/CubeStandard.gcode"));
        SyntaxParser sp = new SyntaxParser(p);
        System.out.println(sp.parse());
    }

}
