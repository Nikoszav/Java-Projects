package mapl.ast;

import mapl.ast.util.Visitor;

public class ExpNot extends Exp {

    public final Exp e;

    public ExpNot(Exp ae) {
        e = ae;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
