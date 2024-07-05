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
import javafx.geometry.Point3D;
import java.lang.Math;
import java.util.List;

// TODO: Convert to javafx.Point3D?
public class ToolPathViewer extends Application {

    static LazyParser parser;
    private static double VECTOR_RADIUS = 0.03;

    static int zoom = -30;

    // Probably not structurally optimal
    public static void addParser(LazyParser p) {
        parser = p;
    }

    public static void setZoom(int z) {
        zoom = z;
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        Scene scene = new Scene(root, 1600, 900);

        // This camera by default is small. <6, 6, 6> ray originating from origin gets close
        // to window bounds

        PerspectiveCamera camera = new PerspectiveCamera(true);
        // TODO Customizing zoom might be a way to constrain large prints to be visible in view
        // Possibly cannot translate beyond -100
        camera.setTranslateZ(zoom);
        camera.setTranslateX(75);
        camera.setTranslateY(5);
        scene.setCamera(camera);

        stage.setScene(scene);

        double factor = 1;

        int i = 0;
        for (List<Vector3D> lr : parser.tpc().toolPath.values()) {
            for (Vector3D v : lr) {
                System.out.println("This layer contains " + lr.size() + " vectors");
                System.out.println("Placing a vector from" + v.start + " to " + v.end);
                System.out.println("It has the dimensions " + v + " for the length " + v.length());
                renderVector(root, v);
            }
        }
        // guideline
        Vector3D a = new Vector3D(new Coord3D(0.0, 0.0, 0.0), new Coord3D(6.0, 6.0, 6.0));
        // Vector3D b = new Vector3D(new Coord3D(0.0, 0.0, 0.0), new Coord3D(2.0, 3.0, 0.0));
        //
        renderVector(root, a);
        // renderVector(root, b);

        stage.setTitle("G-Code Viewer Demo ");
        stage.show();
    }

    void displayToolPath(String[] args) {
        launch(args);
    }

    // With credit for math to Rahel Luthy, https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/.
    // This combines homebrew Coord3D and JavaFX Point3D in an inelegant way,
    // TODO resolve somehow.
    private void renderVector(Group parent, Vector3D vector) {
        Cylinder cylinder = new Cylinder(VECTOR_RADIUS, vector.length());

        // move to midpoint
        Coord3D center = vector.center();
        cylinder.getTransforms().add(new Translate(center.x, center.y, center.z));

        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = new Point3D(vector.dx(), vector.dy(), vector.dz());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
        cylinder.getTransforms().add(rotateAroundCenter);

        parent.getChildren().addAll(cylinder);
    }

    // private void renderVector(Group parent, Vector3D vector) {
    //     Cylinder cylinder = new Cylinder(VECTOR_RADIUS, vector.length());
    //     Cylinder prot = new Cylinder(VECTOR_RADIUS, vector.length()); // prototype
    //     parent.getChildren().addAll(prot); // compare to the baseline
    //
    //     System.out.println(vector.length() + " long");
    //
    //     // Cylinder is by default centered - not ending - at the origin
    //     Vector3D cylinderPos = new Vector3D(new Coord3D(0.0, 0.0, 0.0),
    //         new Coord3D(0.0, vector.length(), 0.0));
    //     // double rot = angleBetweenVectors(vector, cylinderPos);
    //     Coord3D end = new Coord3D(0.0, vector.length(), 0.0);
    //     //System.out.println(end);
    //     //System.out.println(cylinderPos);
    //
    //     Coord3D center = vector.center();
    //     System.out.println("I think the center of " + vector + " is " + center + "!");
    //     cylinder.getTransforms().add(new Translate(center.x, center.y, center.z));
    //
    //     double rotX = rot(vector.dy(), cylinderPos.dy(), vector.dz(), cylinderPos.dz());
    //     //System.out.println("rotX between " + vector + " and " + cylinderPos + " is " + rotX);
    //     double rotY = rot(vector.dx(), cylinderPos.dx(), vector.dz(), cylinderPos.dz());
    //     //System.out.println("rotY between " + vector + " and " + cylinderPos + " is " + rotY);
    //     double rotZ = rot(vector.dx(), cylinderPos.dx(), vector.dy(), cylinderPos.dy());
    //
    //
    //     // // Seemingly related to centering + java coordinate system
    //     // cylinder.getTransforms().add(new Rotate(-rotX / 2, 0.0, vector.dy(), vector.dz()));
    //     // cylinder.getTransforms().add(new Rotate(-rotY / 2, vector.dx(), 0.0, vector.dz()));
    //     // cylinder.getTransforms().add(new Rotate(-rotZ / 2, vector.dx(), vector.dy(), 0.0));
    //
    //     cylinder.getTransforms().add(new Rotate(-rotX, new Point3D(0.0, 1.0, 1.0)));
    //     cylinder.getTransforms().add(new Rotate(-rotY, new Point3D(1.0, 0.0, 1.0)));
    //     cylinder.getTransforms().add(new Rotate(-rotZ, new Point3D(1.0, 1.0, 0.0)));
    //
    //     // System.out.println(rotX + " " + rotY + " " + rotZ);
    //     // cylinder.getTransforms().add(new Rotate(Math.random()*180.0 - 90.0));
    //
    //     parent.getChildren().addAll(cylinder);
    // }

    private double angleBetweenVectors(Vector3D a, Vector3D b) {
        double dotProduct = a.dx() * b.dx() + a.dy() * b.dy() + a.dz() * b.dz();
        // System.out.println("Finding " + dotProduct);
        // System.out.println("Over " + (a.length() * b.length()));
        if (a.length() == 0.0 || b.length() == 0.0) {
            System.out.println("Found a zero vector!!");
            return 0.0;
        }
        System.out.println("The result is " + Math.acos(dotProduct/(a.length() * b.length())));
        return Math.toDegrees(Math.acos(dotProduct/(a.length() * b.length())));
    }

    private double rot(double dia, double dib, double dja, double djb) {
        // where i and j are x, y, or z (probably)
        // eg "dx_a, dx_b"
        // To rotate around Z provide dx and dy, etc

        // Project the vectors onto a plane
        // We'll always pretend we're projecting vectors onto the xy plane
        // it isn't directly relevant, we just need the angle between them in a projection
        // treating them as if they were 2D
        System.out.println("Getting the rot() for " + dia + ", " + dja + " and " + dib + ", " + djb);
        Vector3D projectionA = new Vector3D(new Coord3D(0.0, 0.0, 0.0), new Coord3D(dia, dja, 0.0));
        Vector3D projectionB = new Vector3D(new Coord3D(0.0, 0.0, 0.0), new Coord3D(dib, djb, 0.0));
        System.out.println(projectionA + " -> " + projectionB);

        return angleBetweenVectors(projectionA, projectionB);
    }

}
