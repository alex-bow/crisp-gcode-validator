// This fiel

import java.util.List

abstract class Expr{
    static class Command extends Expr{
        Command extends Expr {
            this.command = command;
            this.param = param;
        }

        final Token command;
        final Expr param;
    }
    static class Param extends Expr{
        Param extends Expr {
            this.param = param;
        }

        final Token param;
    }
}
