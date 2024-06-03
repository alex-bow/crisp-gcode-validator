import java.util.ArrayList;

class Line {
    boolean comment;
    GCodeCommand cmd;
    ArrayList<String> params;

    Line(boolean isComment, GCodeCommand command, ArrayList<String> cmdParams) {
        comment = isComment;
        cmd = command;
        params = cmdParams;
    }
}
