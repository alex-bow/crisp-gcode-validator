package crisp;

import java.io.File;
import javafx.application.Application;

public class ViewerDemo {
    public static void main(String[] args) {
        LazyParser parser = new LazyParser(new File(args[0]));
        ToolPathViewer.addParser(parser);
        // ToolPathViewer.setZoom(Integer.parseInt(args[1]));
        Application.launch(ToolPathViewer.class, args);
    }
}
