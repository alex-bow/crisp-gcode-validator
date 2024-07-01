package crisp;

import java.util.List;
import java.util.ArrayList;

class ToolPathConsumer extends ConsumerModule<List<GCodeCommand>> {
    List<Vector3D> toolPath = new ArrayList<Vector3D>();

    ToolPathConsumer(LazyParser p) {
        super(p);
        data = new ArrayList<GCodeCommand>();
    }

    void examineToken(Token t) {
        if (t.type instanceof PrinterGCodeToken) {
            // we don't care about any other types
            if (check(PrinterGCodeToken.M_CMD) || check(PrinterGCodeToken.G_CMD)) {
                GCodeCommand cmd = new GCodeCommand(t);
                advance();
                while (isParam()) {
                    cmd.addParam(peek());
                    advance();
                }
                data.add(cmd);
                // System.out.println(cmd);
            }
        }
    }

    void parseTokens() {
        super.parseTokens();
        generateToolPath();
    }

    public void generateToolPath() {
        Coord3D start = new Coord3D(0.0, 0.0, 0.0);
        Coord3D current = start;
        for (GCodeCommand cmd : data) {
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            if (cmd.type == PrinterGCodeToken.G_CMD && cmd.idx == 1) {
                // Linear motion
                for (Token param : cmd.params) {
                    if (param.type == PrinterGCodeToken.X_PM) {
                        x += param.value;
                    } else if (param.type == PrinterGCodeToken.Y_PM) {
                        y += param.value;
                    } else if (param.type == PrinterGCodeToken.Z_PM) {
                        z += param.value;
                    }
                }
                start = current;
                current = new Coord3D(current.x + x, current.y + y, current.z + z);
                toolPath.add(new Vector3D(start, current));
            }
        }
        System.out.println("There are " + toolPath.size() + " vectors in the toolpath.");
        double l = 0.0;
        for (Vector3D v : toolPath) {
            l += v.length();
        }
        System.out.println("The extruder travels a total of " + (l / (10 * 100 * 1000)) +
            " km in this print.");
    }

    boolean isParam() {
        return check(PrinterGCodeToken.X_PM) || check(PrinterGCodeToken.Y_PM) ||
            check(PrinterGCodeToken.Z_PM) || check(PrinterGCodeToken.R_PM) ||
            check(PrinterGCodeToken.I_PM) || check(PrinterGCodeToken.Z_PM) ||
            check(PrinterGCodeToken.PARAM);
    }
}
