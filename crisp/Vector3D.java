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
        return end.x - start.x;
    }

    public double dz() {
        return end.z - start.z;
    }

    public double length() {
        return Math.sqrt(Math.pow(start.x + end.x, 2) + Math.pow(start.y + end.y, 2) +
            Math.pow(start.z + end.z, 2));
    }
}
