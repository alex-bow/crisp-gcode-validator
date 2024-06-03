import java.io.File;
import java.util.ArrayList;

class Demo {
    public static void main(String[] args) {
        LazyParser p = new LazyParser(new File("samples/90PercentInfill.gcode"));
        ArrayList<Validator> v = new ArrayList<Validator>();
        v.add(new LibraryValidator());
        ValidationBox vb = new ValidationBox(p, v);
        vb.validate();
    }
}