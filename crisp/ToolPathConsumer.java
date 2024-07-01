package crisp;

import java.util.List;
import java.util.ArrayList;

class ToolPathConsumer extends ConsumerModule<List<GCodeCommand>> {
    List<Vector2D> toolPath = new ArrayList<Vector2D>();

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
        for (GCodeCommand cmd : data) {
            System.out.println("Adding " + cmd + " to toolpath");
        }
    }

    boolean isParam() {
        return check(PrinterGCodeToken.X_PM) || check(PrinterGCodeToken.Y_PM) ||
            check(PrinterGCodeToken.Z_PM) || check(PrinterGCodeToken.R_PM) ||
            check(PrinterGCodeToken.I_PM) || check(PrinterGCodeToken.Z_PM) ||
            check(PrinterGCodeToken.PARAM);
    }
}

class GCodeCommand {
    TokenBase type;
    int idx;
    int line;
    private List<Token> params = new ArrayList<Token>();

    GCodeCommand(Token t) {
        type = t.type;
        idx = t.idx;
        line = t.line;
    }

    public void addParam(Token t) {
        params.add(t);
    }

    public String toString() {
        return type + " " + idx + " -> " + paramsString();
    }

    private String paramsString() {
        String pretty = "";
        for (Token t : params) {
            pretty += t + " ";
        }
        return pretty;
    }


}
