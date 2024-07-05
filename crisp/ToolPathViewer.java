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
import java.lang.Math;

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

        renderVector(root, new Vector3D(new Coord3D(0.0, 0.0, 0.0), new Coord3D(10.0, 0.0, 0.0)));

        stage.setScene(scene);

        Vector3D a = new Vector3D(new Coord3D(0.0, 0.0, 0.0), new Coord3D(10.0, 0.0, 0.0));
        Vector3D b = new Vector3D(new Coord3D(0.0, 0.0, 0.0), new Coord3D(0.0, 10.0, 0.0));

        stage.setTitle("G-Code Viewer Demo " + angleBetweenVectors(a, b));
        stage.show();
    }

    void displayToolPath(String[] args) {
        launch(args);
    }

    private void renderVector(Group parent, Vector3D vector) {
        Cylinder cylinder = new Cylinder(VECTOR_RADIUS, vector.length());


        parent.getChildren().addAll(cylinder);
    }

    private double angleBetweenVectors(Vector3D a, Vector3D b) {
        double dotProduct = a.dx() * b.dx() + a.dy() * b.dy() + a.dz() * b.dz();
        return Math.toDegrees(Math.acos(dotProduct/(a.length() * b.length())));
    }

}
