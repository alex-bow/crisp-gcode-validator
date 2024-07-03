package crisp;

import java.io.File;
import javafx.application.Application;

public class ViewerDemo {
    public static void main(String[] args) {
        LazyParser parser = new LazyParser(new File("./samples/MLib/CubeStandard.gcode"));
        ToolPathViewer.addParser(parser);
        Application.launch(ToolPathViewer.class, args);
    }
}
