package crisp;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ToolPathViewer extends Application {

    static LazyParser parser;
    private static double VECTOR_RADIUS = 0.03;

    // Probably not structurally optimal
    public static void addParser(LazyParser p) {
        parser = p;
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        Scene scene = new Scene(root, 600, 400);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-20);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.setTitle("G-Code Viewer Demo");
        stage.show();
    }

    void displayToolPath(String[] args) {
        launch(args);
    }
}
