import java.io.File;

class Demo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File("samples/CubeCorrect.gcode"));
    }
}