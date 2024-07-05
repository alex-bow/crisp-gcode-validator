package crisp;

class Coord3D {
    public double x;
    public double y;
    public double z;

    Coord3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
