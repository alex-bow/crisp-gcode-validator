package crisp;

import java.util.List;
import java.util.ArrayList;

class GCodeCommand {
    TokenBase type;
    int idx;
    int line;
    List<Token> params = new ArrayList<Token>();

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
