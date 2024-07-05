package crisp;

import java.lang.Math;

class Vector3D {
    Coord3D start;
    Coord3D end;

    Vector3D(Coord3D start, Coord3D end) {
        this.start = start;
        this.end = end;
    }

    public double dx() {
        return end.x - start.x;
    }

    public double dy() {
        return end.y - start.y;
    }

    public double dz() {
        return end.z - start.z;
    }

    public double length() {
        // System.out.println("Finding the length of " + this);
        // System.out.println(Math.pow(start.x + end.x, 2));
        // System.out.println(Math.pow(start.y + end.y, 2));
        // System.out.println(Math.pow(start.z + end.z, 2));
        return Math.sqrt(Math.pow(start.x + end.x, 2) + Math.pow(start.y + end.y, 2) +
            Math.pow(start.z + end.z, 2));
    }

    public Coord3D center() {
        return new Coord3D(start.x + dx() / 2, start.y + dy() / 2, start.z + dz() / 2);
    }

    public String toString() {
        return "<" + dx() + ", " + dy() + ", " + dz() + ">";
    }
}
